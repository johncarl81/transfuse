/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.gen;

import android.os.Bundle;
import android.os.Parcelable;
import com.google.common.collect.ImmutableMap;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTPrimitiveType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtraAspect;
import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.gen.componentBuilder.ExpressionVariableDependentGenerator;
import org.androidtransfuse.intentFactory.AbstractIntentFactoryStrategy;
import org.androidtransfuse.intentFactory.ActivityIntentFactoryStrategy;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;


/**
 * @author John Ericksen
 */
public class IntentFactoryStrategyGenerator implements ExpressionVariableDependentGenerator {

    private static final String STRATEGY_EXT = "Strategy";

    private final Class<? extends AbstractIntentFactoryStrategy> factoryStrategyClass;
    private final JCodeModel codeModel;
    private final ASTClassFactory astClassFactory;
    private final ClassGenerationUtil generationUtil;
    private final ImmutableMap<ASTPrimitiveType, String> methodMapping;

    @Inject
    public IntentFactoryStrategyGenerator(@Assisted Class<? extends AbstractIntentFactoryStrategy> factoryStrategyClass,
                                          JCodeModel codeModel,
                                          ASTClassFactory astClassFactory, ClassGenerationUtil generationUtil) {
        this.factoryStrategyClass = factoryStrategyClass;
        this.codeModel = codeModel;
        this.astClassFactory = astClassFactory;
        this.generationUtil = generationUtil;

        ImmutableMap.Builder<ASTPrimitiveType, String> methodMappingBuilder = ImmutableMap.builder();
        methodMappingBuilder.put(ASTPrimitiveType.BOOLEAN, "putBoolean");
        methodMappingBuilder.put(ASTPrimitiveType.BYTE, "putByte");
        methodMappingBuilder.put(ASTPrimitiveType.CHAR, "putChar");
        methodMappingBuilder.put(ASTPrimitiveType.DOUBLE, "putDouble");
        methodMappingBuilder.put(ASTPrimitiveType.FLOAT, "putFloat");
        methodMappingBuilder.put(ASTPrimitiveType.INT, "putInt");
        methodMappingBuilder.put(ASTPrimitiveType.LONG, "putLong");
        methodMappingBuilder.put(ASTPrimitiveType.SHORT, "putShort");

        this.methodMapping = methodMappingBuilder.build();
    }

    @Override
    public void generate(JDefinedClass definedClass, MethodDescriptor methodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {

        try {
            JDefinedClass strategyClass = generationUtil.defineClass(descriptor.getPackageClass().append(STRATEGY_EXT));

            strategyClass._extends(factoryStrategyClass);

            JInvocation getExtrasMethod = JExpr.invoke(ActivityIntentFactoryStrategy.GET_EXTRAS_METHOD);

            List<IntentFactoryExtraAspect> extras = getExtras(expressionMap);

            //constructor, with required extras
            JMethod constructor = strategyClass.constructor(JMod.PUBLIC);
            JBlock constructorBody = constructor.body();
            JDocComment javadocComments = constructor.javadoc();
            javadocComments.append("Strategy Class for generating Intent for " + descriptor.getPackageClass().getClassName());

            constructorBody.add(JExpr.invoke("super")
                    .arg(codeModel.ref(descriptor.getPackageClass().getFullyQualifiedName()).dotclass())
                    .arg(JExpr._new(codeModel._ref(Bundle.class)))
            );

            for (IntentFactoryExtraAspect extra : extras) {
                if (extra.isRequired()) {
                    JVar extraParam = constructor.param(codeModel.ref(extra.getType().getName()), extra.getName());

                    constructorBody.add(buildBundleMethod(getExtrasMethod, extra.getType(), extra.getName(), extraParam));

                    javadocComments.addParam(extraParam);
                } else {
                    //setter for non-required extra
                    JMethod setterMethod = strategyClass.method(JMod.PUBLIC, strategyClass, "set" + upperFirst(extra.getName()));
                    JVar extraParam = setterMethod.param(codeModel.ref(extra.getType().getName()), extra.getName());

                    JBlock setterBody = setterMethod.body();
                    setterBody.add(buildBundleMethod(getExtrasMethod, extra.getType(), extra.getName(), extraParam));
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
        return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }

    private JStatement buildBundleMethod(JInvocation extras, ASTType type, String name, JVar extraParam) {

        //autoboxable (Long, Integer, etc)
        ASTPrimitiveType primitiveType = ASTPrimitiveType.getAutoboxType(type.getName());

        if (type instanceof ASTPrimitiveType) {
            primitiveType = (ASTPrimitiveType) type;
        }

        if (primitiveType != null) {
            return extras.invoke(methodMapping.get(primitiveType)).arg(name).arg(extraParam);
        } else if (type.getName().equals(String.class.getName())) {
            return extras.invoke("putString").arg(name).arg(extraParam);
        } else if (type.implementsFrom(astClassFactory.getType(Serializable.class))) {
            return extras.invoke("putSerializable").arg(name).arg(extraParam);
        }
        if (type.inheritsFrom(astClassFactory.getType(Parcelable.class))) {
            return extras.invoke("putParcelable").arg(name).arg(extraParam);
        }
        if (type.isAnnotated(Parcel.class)) {
            JInvocation wrappedParcel = codeModel.ref(ParcelsGenerator.PARCELS_NAME.toString())
                    .staticInvoke(ParcelsGenerator.WRAP_METHOD).arg(extraParam);

            return extras.invoke("putParcelable").arg(name).arg(wrappedParcel);
        }

        throw new TransfuseAnalysisException("Unable to find appropriate type to build intent factory strategy: " + type.getName());
    }

    private List<IntentFactoryExtraAspect> getExtras(Map<InjectionNode, TypedExpression> expressionMap) {
        Set<IntentFactoryExtraAspect> uniqueExtras = new HashSet<IntentFactoryExtraAspect>();
        List<IntentFactoryExtraAspect> extras = new ArrayList<IntentFactoryExtraAspect>();
        for (InjectionNode injectionNode : expressionMap.keySet()) {
            IntentFactoryExtraAspect intentFactoryExtra = injectionNode.getAspect(IntentFactoryExtraAspect.class);
            if (intentFactoryExtra != null && !uniqueExtras.contains(intentFactoryExtra)) {
                uniqueExtras.add(intentFactoryExtra);
                extras.add(intentFactoryExtra);
            }
        }
        Collections.sort(extras);
        return extras;
    }
}