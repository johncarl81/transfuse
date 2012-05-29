package org.androidtransfuse.gen;

import android.os.Bundle;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTPrimitiveType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtra;
import org.androidtransfuse.gen.componentBuilder.ExpressionVariableDependentGenerator;
import org.androidtransfuse.intentFactory.ActivityIntentFactoryStrategy;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

/**
 * @author John Ericksen
 */
public class IntentFactoryStrategyGenerator implements ExpressionVariableDependentGenerator {

    private JCodeModel codeModel;
    private UniqueVariableNamer namer;
    private GeneratedClassAnnotator generatedClassAnnotator;
    private ASTClassFactory astClassFactory;

    private enum PrimitiveExtraMethod {
        PUTBOOLEAN(ASTPrimitiveType.BOOLEAN, "putBoolean"),
        PUTBYTE(ASTPrimitiveType.BYTE, "putByte"),
        PUTCHAR(ASTPrimitiveType.CHAR, "putChar"),
        PUTDOUBLE(ASTPrimitiveType.DOUBLE, "putDouble"),
        PUTFLOAT(ASTPrimitiveType.FLOAT, "putFloat"),
        PUTINT(ASTPrimitiveType.INT, "putInt"),
        PUTLONG(ASTPrimitiveType.LONG, "putLong"),
        PUTSHORT(ASTPrimitiveType.SHORT, "putShort");

        private static final Map<ASTPrimitiveType, PrimitiveExtraMethod> METHOD_MAPPING = new EnumMap<ASTPrimitiveType, PrimitiveExtraMethod>(ASTPrimitiveType.class);

        private ASTPrimitiveType mappedPrimitiveType;
        private String methodName;

        static {
            Set<ASTPrimitiveType> primitiveSet = new HashSet<ASTPrimitiveType>(Arrays.asList(ASTPrimitiveType.values()));
            for (PrimitiveExtraMethod primitiveExtraMethod : values()) {
                if (METHOD_MAPPING.containsKey(primitiveExtraMethod.getMappedPrimitiveType())) {
                    throw new TransfuseAnalysisException("Duplicate Mapping of Primitive Type");
                }
                METHOD_MAPPING.put(primitiveExtraMethod.getMappedPrimitiveType(), primitiveExtraMethod);
                primitiveSet.remove(primitiveExtraMethod.getMappedPrimitiveType());
            }

            if (!primitiveSet.isEmpty()) {
                throw new TransfuseAnalysisException("Unmapped primitive types found.");
            }
        }

        private PrimitiveExtraMethod(ASTPrimitiveType mappedPrimitiveType, String methodName) {
            this.mappedPrimitiveType = mappedPrimitiveType;
            this.methodName = methodName;
        }

        public ASTPrimitiveType getMappedPrimitiveType() {
            return mappedPrimitiveType;
        }

        public String getMethodName() {
            return methodName;
        }

        public static PrimitiveExtraMethod get(ASTPrimitiveType primitiveType) {
            return METHOD_MAPPING.get(primitiveType);
        }
    }

    @Inject
    public IntentFactoryStrategyGenerator(JCodeModel codeModel, UniqueVariableNamer namer, GeneratedClassAnnotator generatedClassAnnotator, ASTClassFactory astClassFactory) {
        this.codeModel = codeModel;
        this.namer = namer;
        this.generatedClassAnnotator = generatedClassAnnotator;
        this.astClassFactory = astClassFactory;
    }

    @Override
    public void generate(JDefinedClass definedClass, JBlock block, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor, RResource rResource) {

        try {
            JDefinedClass strategyClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName() + "Strategy", ClassType.CLASS);

            generatedClassAnnotator.annotateClass(strategyClass);

            //todo: different extends for different component types (Activity, Service, etc)
            strategyClass._extends(ActivityIntentFactoryStrategy.class);

            JInvocation getExtrasMethod = JExpr.invoke("getExtras");

            List<IntentFactoryExtra> extras = getExtras(expressionMap);

            //constructor, with required extras
            JMethod constructor = strategyClass.constructor(JMod.PUBLIC);
            JBlock constructorBody = constructor.body();
            JDocComment javadocComments = constructor.javadoc();
            javadocComments.append("Strategy Class for generating Intent for " + descriptor.getPackageClass().getClassName());

            constructorBody.add(JExpr.invoke("super")
                    .arg(codeModel.ref(descriptor.getPackageClass().getFullyQualifiedName()).dotclass())
                    .arg(JExpr._new(codeModel._ref(Bundle.class)))
            );

            for (IntentFactoryExtra extra : extras) {
                if (extra.isRequired()) {
                    JVar extraParam = constructor.param(codeModel.ref(extra.getType().getName()), extra.getName());

                    constructorBody.add(getExtrasMethod.invoke(getBundleMethod(extra.getType())).arg(extra.getName()).arg(extraParam));

                    javadocComments.addParam(extraParam);
                } else {
                    //setter for non-required extra
                    JMethod setterMethod = strategyClass.method(JMod.PUBLIC, strategyClass, "set" + upperFirst(extra.getName()));
                    JVar extraParam = setterMethod.param(codeModel.ref(extra.getType().getName()), extra.getName());

                    JBlock setterBody = setterMethod.body();
                    setterBody.add(getExtrasMethod.invoke(getBundleMethod(extra.getType())).arg(extra.getName()).arg(extraParam));
                    setterMethod.javadoc().append("Optional Extra parameter");
                    setterMethod.javadoc().addParam(extraParam);

                    setterBody._return(JExpr._this());

                }
            }

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class already defined while trying to define IntentFactoryStrategy", e);
        }
    }

    private String upperFirst(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getBundleMethod(ASTType type) {
        String methodName = "putParcelable";

        //autoboxable (Long, Integer, etc)
        ASTPrimitiveType primitiveType = ASTPrimitiveType.getAutoboxType(type.getName());

        if (type instanceof ASTPrimitiveType) {
            primitiveType = (ASTPrimitiveType) type;
        }

        if (primitiveType != null) {
            methodName = PrimitiveExtraMethod.get(primitiveType).getMethodName();
        } else if (type.getName().equals(String.class.getName())) {
            methodName = "putString";
        } else if (type.implementsFrom(astClassFactory.buildASTClassType(Serializable.class))) {
            methodName = "putSerializable";
        }

        return methodName;
    }

    private List<IntentFactoryExtra> getExtras(Map<InjectionNode, TypedExpression> expressionMap) {
        Set<IntentFactoryExtra> uniqueExtras = new HashSet<IntentFactoryExtra>();
        List<IntentFactoryExtra> extras = new ArrayList<IntentFactoryExtra>();
        for (InjectionNode injectionNode : expressionMap.keySet()) {
            IntentFactoryExtra intentFactoryExtra = injectionNode.getAspect(IntentFactoryExtra.class);
            if (intentFactoryExtra != null && !uniqueExtras.contains(intentFactoryExtra)) {
                uniqueExtras.add(intentFactoryExtra);
                extras.add(intentFactoryExtra);
            }
        }
        Collections.sort(extras);
        return extras;
    }
}