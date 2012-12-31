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

import android.content.SharedPreferences;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Preference;
import org.androidtransfuse.model.InjectionNode;

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
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        String preferenceName = annotation.getProperty("value", String.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);

        InjectionNode preferenceManagerInjectionNode = injectionPointFactory.buildInjectionNode(SharedPreferences.class, context);

        injectionNode.addAspect(VariableBuilder.class,
                variableInjectionBuilderFactory.buildPreferenceVariableBuilder(astType, preferenceName, preferenceManagerInjectionNode));

        return injectionNode;
    }
}
