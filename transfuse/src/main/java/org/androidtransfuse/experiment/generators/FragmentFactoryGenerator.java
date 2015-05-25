/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtraAspect;
import org.androidtransfuse.analysis.repository.BundlePropertyBuilderRepository;
import org.androidtransfuse.analysis.repository.ParcelerPropertyBuilder;
import org.androidtransfuse.analysis.repository.PropertyBuilder;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ComponentPartGenerator;
import org.androidtransfuse.experiment.Generation;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.util.StringUtil;

import javax.inject.Inject;
import java.util.*;

/**
 * @author John Ericksen
 */
public class FragmentFactoryGenerator implements Generation {

    private static final String BUILD_FRAGMENT = "build";
    private static final String FACTORY_EXT = "Factory";

    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;
    private final BundlePropertyBuilderRepository repository;
    private final ParcelerPropertyBuilder parcelerPropertyBuilder;

    @Inject
    public FragmentFactoryGenerator(ClassGenerationUtil generationUtil, UniqueVariableNamer namer, BundlePropertyBuilderRepository repository, ParcelerPropertyBuilder parcelerPropertyBuilder) {
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.repository = repository;
        this.parcelerPropertyBuilder = parcelerPropertyBuilder;
    }

    @Override
    public String getName() {
        return "FragmentFactory";
    }

    @Override
    public void schedule(final ComponentBuilder builder, ComponentDescriptor descriptor) {
        builder.add(new ComponentPartGenerator() {
            @Override
            public void generate(ComponentDescriptor descriptor) {
                try {
                    JDefinedClass factoryClass = generationUtil.defineClass(descriptor.getPackageClass().append(FACTORY_EXT));

                    List<IntentFactoryExtraAspect> extras = getExtras(builder.getExpressionMap());

                    JMethod constructor = factoryClass.constructor(JMod.PUBLIC);
                    JBlock constructorBody = constructor.body();
                    JDocComment javadocComments = constructor.javadoc();

                    // bundle
                    JClass bundleRef = generationUtil.ref(AndroidLiterals.BUNDLE);
                    JFieldVar bundle = factoryClass.field(JMod.PRIVATE | JMod.FINAL, bundleRef, namer.generateName(AndroidLiterals.BUNDLE), JExpr._new(bundleRef));

                    createBuilderMethod(descriptor, factoryClass, bundle);

                    for (IntentFactoryExtraAspect extraAspect : extras) {
                        if (extraAspect.isRequired()) {
                            JVar extraParam = constructor.param(generationUtil.ref(extraAspect.getType()), extraAspect.getName());

                            constructorBody.add(buildBundleMethod(extraAspect, bundle, extraParam));

                            javadocComments.addParam(extraParam);
                        } else {
                            //setter for non-required extra
                            JMethod setterMethod = factoryClass.method(JMod.PUBLIC, factoryClass, "add" + StringUtil.upperFirst(extraAspect.getName()));
                            JVar extraParam = setterMethod.param(generationUtil.ref(extraAspect.getType()), extraAspect.getName());

                            JBlock setterBody = setterMethod.body();
                            setterBody.add(buildBundleMethod(extraAspect, bundle, extraParam));
                            setterMethod.javadoc().append("Optional Extra parameter");
                            setterMethod.javadoc().addParam(extraParam);

                            setterBody._return(JExpr._this());

                        }
                    }

                } catch (JClassAlreadyExistsException e) {
                    throw new TransfuseAnalysisException("Class already defined while trying to define IntentFactoryStrategy", e);
                }
            }
        });
    }

    private void createBuilderMethod(ComponentDescriptor descriptor, JDefinedClass factoryClass, JFieldVar bundle) {
        JClass targetRef = generationUtil.ref(descriptor.getPackageClass());
        JBlock buildMethodBody = factoryClass.method(JMod.PUBLIC, targetRef, BUILD_FRAGMENT).body();

        JVar fragmentVar = buildMethodBody.decl(targetRef, namer.generateName(descriptor.getType()), JExpr._new(targetRef));
        buildMethodBody.invoke(fragmentVar, "setArguments").arg(bundle);

        buildMethodBody._return(fragmentVar);
    }

    private JStatement buildBundleMethod(IntentFactoryExtraAspect extraAspect, JExpression extras, JVar extraParam) {

        PropertyBuilder builder;
        if(extraAspect.isForceParceler()){
            builder = parcelerPropertyBuilder;
        }
        else {
            builder = repository.get(extraAspect.getType());
        }

        if(builder == null){
            throw new TransfuseAnalysisException("Unable to find appropriate type to build intent factory strategy: " + extraAspect.getType().getName());
        }

        return builder.buildWriter(extras, extraAspect.getName(), extraParam);
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
