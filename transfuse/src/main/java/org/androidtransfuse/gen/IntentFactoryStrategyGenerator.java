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
package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtraAspect;
import org.androidtransfuse.analysis.repository.BundlePropertyBuilderRepository;
import org.androidtransfuse.analysis.repository.PropertyBuilder;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ComponentPartGenerator;
import org.androidtransfuse.experiment.Generation;
import org.androidtransfuse.intentFactory.AbstractIntentFactoryStrategy;
import org.androidtransfuse.intentFactory.ActivityIntentFactoryStrategy;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;
import java.util.*;


/**
 * @author John Ericksen
 */
public class IntentFactoryStrategyGenerator implements Generation {

    public static final PackageClass ATPARCEL_NAME = new PackageClass("org.parceler", "Parcel");
    public static final PackageClass PARCELS_NAME = new PackageClass("org.parceler", "Parcels");
    public static final String WRAP_METHOD = "wrap";

    private static final String STRATEGY_EXT = "Strategy";

    private final Class<? extends AbstractIntentFactoryStrategy> factoryStrategyClass;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;
    private final BundlePropertyBuilderRepository repository;

    @Inject
    public IntentFactoryStrategyGenerator(/*@Assisted*/ Class factoryStrategyClass,
                                          ClassGenerationUtil generationUtil,
                                          UniqueVariableNamer namer,
                                          BundlePropertyBuilderRepository repository) {
        this.factoryStrategyClass = factoryStrategyClass;
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.repository = repository;


    }

    @Override
    public void schedule(final ComponentBuilder builder, ComponentDescriptor descriptor) {
        builder.add(new ComponentPartGenerator() {
            @Override
            public void generate(org.androidtransfuse.experiment.ComponentDescriptor descriptor) {
                try {
                    JDefinedClass strategyClass = generationUtil.defineClass(descriptor.getPackageClass().append(STRATEGY_EXT));
                    strategyClass.annotate(SuppressWarnings.class).param("value", "unchecked");

                    strategyClass._extends(factoryStrategyClass);

                    JInvocation getExtrasMethod = JExpr.invoke(ActivityIntentFactoryStrategy.GET_EXTRAS_METHOD);

                    List<IntentFactoryExtraAspect> extras = getExtras(builder.getExpressionMap());

                    //constructor, with required extras
                    JMethod constructor = strategyClass.constructor(JMod.PUBLIC);
                    JBlock constructorBody = constructor.body();
                    JDocComment javadocComments = constructor.javadoc();
                    javadocComments.append("Strategy Class for generating Intent for " + descriptor.getPackageClass().getClassName());

                    constructorBody.add(JExpr.invoke("super")
                            .arg(generationUtil.ref(descriptor.getPackageClass()).dotclass())
                            .arg(JExpr._new(generationUtil.ref(AndroidLiterals.BUNDLE)))
                    );

                    //addFlags method
                    JMethod addFlags = strategyClass.method(JMod.PUBLIC, strategyClass, "addFlags");
                    JVar flagParam = addFlags.param(int.class, namer.generateName(int.class));
                    JBlock addFlagsBody = addFlags.body();
                    addFlagsBody.invoke("internalAddFlags").arg(flagParam);
                    addFlagsBody._return(JExpr._this());

                    //addCategories method
                    JMethod addCategory = strategyClass.method(JMod.PUBLIC, strategyClass, "addCategory");
                    JVar categoryParam = addCategory.param(String.class, namer.generateName(String.class));
                    JBlock addCategoryBody = addCategory.body();
                    addCategoryBody.invoke("internalAddCategory").arg(categoryParam);
                    addCategoryBody._return(JExpr._this());

                    for (IntentFactoryExtraAspect extra : extras) {
                        if (extra.isRequired()) {
                            JVar extraParam = constructor.param(generationUtil.ref(extra.getType()), extra.getName());

                            constructorBody.add(buildBundleMethod(getExtrasMethod, extra.getType(), extra.getName(), extraParam));

                            javadocComments.addParam(extraParam);
                        } else {
                            //setter for non-required extra
                            JMethod setterMethod = strategyClass.method(JMod.PUBLIC, strategyClass, "set" + upperFirst(extra.getName()));
                            JVar extraParam = setterMethod.param(generationUtil.ref(extra.getType()), extra.getName());

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
        });
    }

    private String upperFirst(String name) {
        return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }

    private JStatement buildBundleMethod(JInvocation extras, ASTType type, String name, JVar extraParam) {

        PropertyBuilder builder = repository.get(type);

        if(builder == null){
            throw new TransfuseAnalysisException("Unable to find appropriate type to build intent factory strategy: " + type.getName());
        }

        return builder.buildWriter(extras, name, extraParam);
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