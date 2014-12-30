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
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ScopesInjectionNodeBuilder implements InjectionNodeBuilder {

    private final Analyzer analyzer;
    private final TypedExpressionFactory typedExpressionFactory;

    @Inject
    public ScopesInjectionNodeBuilder(Analyzer analyzer, TypedExpressionFactory typedExpressionFactory) {
        this.analyzer = analyzer;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTBase target, InjectionSignature signature, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(signature, context);

        injectionNode.addAspect(VariableBuilder.class, new ScopesVariableBuilder(typedExpressionFactory));

        return injectionNode;
    }
}
