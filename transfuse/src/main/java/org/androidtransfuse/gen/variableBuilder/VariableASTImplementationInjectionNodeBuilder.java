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

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class VariableASTImplementationInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private final Analyzer analyzer;
    private final ASTType implType;
    private final Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;

    @Inject
    public VariableASTImplementationInjectionNodeBuilder(@Assisted ASTType implType,
                                                         Analyzer analyzer,
                                                         Provider<VariableInjectionBuilder> variableInjectionBuilderProvider) {
        this.analyzer = analyzer;
        this.implType = implType;
        this.variableInjectionBuilderProvider = variableInjectionBuilderProvider;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(astType, implType, context);

        //default variable builder
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        return injectionNode;
    }
}
