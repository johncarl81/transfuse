package org.androidtransfuse.gen;

import android.os.Bundle;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTElementType;
import org.androidtransfuse.analysis.adapter.ASTPrimitiveType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtra;
import org.androidtransfuse.gen.componentBuilder.ExpressionVariableDependentGenerator;
import org.androidtransfuse.intentFactory.ActivityIntentFactoryStrategy;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import java.util.*;

/**
 * @author John Ericksen
 */
public class IntentFactoryStrategyGenerator implements ExpressionVariableDependentGenerator {

    private JCodeModel codeModel;
    private UniqueVariableNamer namer;
    private GeneratedClassAnnotator generatedClassAnnotator;

    @Inject
    public IntentFactoryStrategyGenerator(JCodeModel codeModel, UniqueVariableNamer namer, GeneratedClassAnnotator generatedClassAnnotator) {
        this.codeModel = codeModel;
        this.namer = namer;
        this.generatedClassAnnotator = generatedClassAnnotator;
    }

    @Override
    public void generate(JDefinedClass definedClass, Map<InjectionNode, JExpression> expressionMap, ComponentDescriptor descriptor, RResource rResource) {

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

            constructorBody.add(JExpr.invoke("super")
                    .arg(codeModel.ref(descriptor.getPackageClass().getFullyQualifiedName()).staticRef("class"))
                    .arg(JExpr._new(codeModel._ref(Bundle.class)))
            );

            for (IntentFactoryExtra extra : extras) {
                if (extra.isRequired()) {
                    JVar extraParam = constructor.param(codeModel.ref(extra.getType().getName()), extra.getName());

                    constructorBody.add(getExtrasMethod.invoke(getBundleMethod(extra.getType())).arg(extra.getName()).arg(extraParam));
                } else {
                    //setter for non-required extra
                    JMethod setterMethod = strategyClass.method(JMod.PUBLIC, codeModel.VOID, "set" + upperFirst(extra.getName()));
                    JVar extraParam = setterMethod.param(codeModel.ref(extra.getType().getName()), namer.generateName(extra.getType().getName()));

                    setterMethod.body().add(getExtrasMethod.invoke(getBundleMethod(extra.getType())).arg(extra.getName()).arg(extraParam));

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
            switch (primitiveType) {
                case BOOLEAN:
                    methodName = "putBoolean";
                    break;
                case BYTE:
                    methodName = "putByte";
                    break;
                case CHAR:
                    methodName = "putChar";
                    break;
                case DOUBLE:
                    methodName = "putDouble";
                    break;
                case FLOAT:
                    methodName = "putFloat";
                    break;
                case INT:
                    methodName = "putInt";
                    break;
                case LONG:
                    methodName = "putLong";
                    break;
                case SHORT:
                    methodName = "putShort";
                    break;
            }
        }

        if (type instanceof ASTElementType) {


            if (type.getName().equals(String.class.getName())) {
                methodName = "putString";
            }

            /*else if(((ASTElementType)type).implmenets(Serializable.class.getName())){
                methodName = "putSerializable";
            }*/
        }

        /*
        public  void putBoolean(java.lang.String key, boolean value) { throw new RuntimeException("Stub!"); }
        public  void putByte(java.lang.String key, byte value) { throw new RuntimeException("Stub!"); }
        public  void putChar(java.lang.String key, char value) { throw new RuntimeException("Stub!"); }
        public  void putShort(java.lang.String key, short value) { throw new RuntimeException("Stub!"); }
        public  void putInt(java.lang.String key, int value) { throw new RuntimeException("Stub!"); }
        public  void putLong(java.lang.String key, long value) { throw new RuntimeException("Stub!"); }
        public  void putFloat(java.lang.String key, float value) { throw new RuntimeException("Stub!"); }
        public  void putDouble(java.lang.String key, double value) { throw new RuntimeException("Stub!"); }
        public  void putString(java.lang.String key, java.lang.String value) { throw new RuntimeException("Stub!"); }
        public  void putCharSequence(java.lang.String key, java.lang.CharSequence value) { throw new RuntimeException("Stub!"); }
        public  void putParcelable(java.lang.String key, android.os.Parcelable value) { throw new RuntimeException("Stub!"); }
        public  void putParcelableArray(java.lang.String key, android.os.Parcelable[] value) { throw new RuntimeException("Stub!"); }
        public  void putParcelableArrayList(java.lang.String key, java.util.ArrayList<? extends android.os.Parcelable> value) { throw new RuntimeException("Stub!"); }
        public  void putSparseParcelableArray(java.lang.String key, android.util.SparseArray<? extends android.os.Parcelable> value) { throw new RuntimeException("Stub!"); }
        public  void putIntegerArrayList(java.lang.String key, java.util.ArrayList<java.lang.Integer> value) { throw new RuntimeException("Stub!"); }
        public  void putStringArrayList(java.lang.String key, java.util.ArrayList<java.lang.String> value) { throw new RuntimeException("Stub!"); }
        public  void putCharSequenceArrayList(java.lang.String key, java.util.ArrayList<java.lang.CharSequence> value) { throw new RuntimeException("Stub!"); }
        public  void putSerializable(java.lang.String key, java.io.Serializable value) { throw new RuntimeException("Stub!"); }
        public  void putBooleanArray(java.lang.String key, boolean[] value) { throw new RuntimeException("Stub!"); }
        public  void putByteArray(java.lang.String key, byte[] value) { throw new RuntimeException("Stub!"); }
        public  void putShortArray(java.lang.String key, short[] value) { throw new RuntimeException("Stub!"); }
        public  void putCharArray(java.lang.String key, char[] value) { throw new RuntimeException("Stub!"); }
        public  void putIntArray(java.lang.String key, int[] value) { throw new RuntimeException("Stub!"); }
        public  void putLongArray(java.lang.String key, long[] value) { throw new RuntimeException("Stub!"); }
        public  void putFloatArray(java.lang.String key, float[] value) { throw new RuntimeException("Stub!"); }
        public  void putDoubleArray(java.lang.String key, double[] value) { throw new RuntimeException("Stub!"); }
        public  void putStringArray(java.lang.String key, java.lang.String[] value) { throw new RuntimeException("Stub!"); }
        public  void putCharSequenceArray(java.lang.String key, java.lang.CharSequence[] value) { throw new RuntimeException("Stub!"); }
        public  void putBundle(java.lang.String key, android.os.Bundle value) { throw new RuntimeException("Stub!"); }
         */

        return methodName;
    }

    private List<IntentFactoryExtra> getExtras(Map<InjectionNode, JExpression> expressionMap) {
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

    private final class ExtraType {
        private boolean required;
        private String name;
        private ASTType type;

        private ExtraType(boolean required, String name, ASTType type) {
            this.required = required;
            this.name = name;
            this.type = type;
        }

        public boolean isRequired() {
            return required;
        }

        public String getName() {
            return name;
        }

        public ASTType getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ExtraType)) return false;

            ExtraType extraType = (ExtraType) o;

            if (!name.equals(extraType.name)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
