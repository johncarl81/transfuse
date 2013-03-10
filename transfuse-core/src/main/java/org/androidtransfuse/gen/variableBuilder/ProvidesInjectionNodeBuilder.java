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

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ProvidesInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private final ASTType moduleType;
    private final ASTMethod providesMethod;
    private final Analyzer analyzer;
    private final InjectionPointFactory injectionNodeFactory;
    private final ProvidesVariableBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ProvidesInjectionNodeBuilder(/*@Assisted*/ ASTType moduleType,
                                        /*@Assisted*/ ASTMethod providesMethod,
                                        Analyzer analyzer,
                                        InjectionPointFactory injectionNodeFactory,
                                        ProvidesVariableBuilderFactory variableInjectionBuilderFactory) {
        this.moduleType = moduleType;
        this.providesMethod = providesMethod;
        this.analyzer = analyzer;
        this.injectionNodeFactory = injectionNodeFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }


    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);

        InjectionNode module = analyzer.analyze(moduleType, moduleType, context);

        Map<ASTParameter, InjectionNode> dependencyAnalysis = new HashMap<ASTParameter, InjectionNode>();

        for (ASTParameter parameter : providesMethod.getParameters()) {
            InjectionNode parameterInjectionNode = injectionNodeFactory.buildInjectionNode(parameter.getASTType(), context);

            dependencyAnalysis.put(parameter, parameterInjectionNode);
        }

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildProvidesVariableBuilder(module, providesMethod, dependencyAnalysis));

        return injectionNode;
    }
}
