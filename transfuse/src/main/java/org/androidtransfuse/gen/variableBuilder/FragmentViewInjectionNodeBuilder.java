/**
 * Copyright 2013 John Ericksen
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
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FragmentViewInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter {

    private final ClassGenerationUtil generationUtil;
    private final InjectionPointFactory injectionPointFactory;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final Analyzer analyzer;

    @Inject
    public FragmentViewInjectionNodeBuilder(ClassGenerationUtil generationUtil,
                                            InjectionPointFactory injectionPointFactory,
                                            VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                            Analyzer analyzer) {
        super(View.class);
        this.generationUtil = generationUtil;
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(InjectionSignature signature, AnalysisContext context, ASTAnnotation annotation) {
        Integer viewId = annotation.getProperty("value", Integer.class);
        String viewTag = annotation.getProperty("tag", String.class);

        InjectionNode injectionNode = analyzer.analyze(signature, context);

        InjectionNode viewInjectionNode = injectionPointFactory.buildInjectionNode(AndroidLiterals.VIEW, context);

        try {
            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildFragmentViewVariableBuilder(viewId, viewTag, viewInjectionNode, codeModel.parseType(signature.getType().getName())));
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse type " + signature.getType().getName(), e);
        }

        return injectionNode;
    }
}
