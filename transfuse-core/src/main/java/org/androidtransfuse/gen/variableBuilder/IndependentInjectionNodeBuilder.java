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

import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class IndependentInjectionNodeBuilder implements InjectionNodeBuilder {

    private final VariableBuilder variableBuilder;
    private final Analyzer analyzer;

    @Inject
    public IndependentInjectionNodeBuilder(/*@Assisted*/ VariableBuilder variableBuilder, Analyzer analyzer) {
        this.variableBuilder = variableBuilder;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTBase target, InjectionSignature signature, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(signature, context);

        injectionNode.addAspect(VariableBuilder.class, variableBuilder);

        return injectionNode;
    }
}
