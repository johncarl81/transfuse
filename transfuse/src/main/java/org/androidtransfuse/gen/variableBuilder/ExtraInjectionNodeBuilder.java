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
package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.adapter.ASTPrimitiveType;
import org.androidtransfuse.adapter.ASTUtils;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtraAspect;
import org.androidtransfuse.analysis.repository.BundlePropertyBuilderRepository;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExtraInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter {

    private final InjectionPointFactory injectionPointFactory;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final Analyzer analyzer;
    private final Validator validator;
    private final BundlePropertyBuilderRepository repository;

    @Inject
    public ExtraInjectionNodeBuilder(InjectionPointFactory injectionPointFactory,
                                     VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                     Analyzer analyzer,
                                     Validator validator,
                                     BundlePropertyBuilderRepository repository) {
        super(Extra.class);
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analyzer = analyzer;
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTBase target, InjectionSignature signature, AnalysisContext context, ASTAnnotation annotation) {
        String extraId = annotation.getProperty("value", String.class);
        Boolean optional = annotation.getProperty("optional", Boolean.class);
        Boolean forceParceler = annotation.getProperty("forceParceler", Boolean.class);

        if (optional == null) {
            optional = false;
        }
        if (forceParceler == null){
            forceParceler = false;
        }

        boolean wrapped = ASTUtils.getInstance().isAnnotated(signature.getType(), "org.parceler.Parcel");

        InjectionNode injectionNode = analyzer.analyze(signature, context);

        if(!forceParceler && optional && signature.getType() instanceof ASTPrimitiveType){
            validator.error("@Extra marked with optional=true must not annotate a primitive type.")
                    .element(target)
                    .annotation(annotation)
                    .build();
        }
        else if(!forceParceler && !repository.matches(signature.getType())){
            validator.error("@Extra type " + signature.getType().getName() + " not available for marshalling.")
                    .element(target)
                    .build();
        }
        else {

            InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(AndroidLiterals.ACTIVITY, context);

            injectionNode.addAspect(IntentFactoryExtraAspect.class, new IntentFactoryExtraAspect(!optional, extraId, forceParceler, signature.getType()));

            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildExtraVariableBuilder(extraId, activityInjectionNode, optional, forceParceler || wrapped));
        }

        return injectionNode;
    }
}
