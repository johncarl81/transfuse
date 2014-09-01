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
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.annotations.Preference;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class PreferenceInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter {

    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final InjectionPointFactory injectionPointFactory;
    private final Analyzer analyzer;

    @Inject
    public PreferenceInjectionNodeBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                          InjectionPointFactory injectionPointFactory,
                                          Analyzer analyzer) {
        super(Preference.class);
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTBase target, InjectionSignature signature, AnalysisContext context, ASTAnnotation annotation) {
        String preferenceName = annotation.getProperty("value", String.class);

        InjectionNode injectionNode = analyzer.analyze(signature, context);

        InjectionNode preferenceManagerInjectionNode = injectionPointFactory.buildInjectionNode(AndroidLiterals.SHARED_PREFERENCES, context);

        injectionNode.addAspect(VariableBuilder.class,
                variableInjectionBuilderFactory.buildPreferenceVariableBuilder(signature.getType(), preferenceName, preferenceManagerInjectionNode));

        return injectionNode;
    }
}
