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
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class DependentInjectionNodeBuilder implements InjectionNodeBuilder{

    private final Class dependency;
    private final Class returnType;
    private final DependentVariableBuilder variableBuilder;
    private final InjectionPointFactory injectionPointFactory;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final Analyzer analyzer;

    @Inject
    public DependentInjectionNodeBuilder(@Assisted("dependency") Class dependency,
                                         @Assisted("returnType") Class returnType,
                                         @Assisted DependentVariableBuilder variableBuilder,
                                         InjectionPointFactory injectionPointFactory,
                                         VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                         Analyzer analyzer) {
        this.dependency = dependency;
        this.returnType = returnType;
        this.variableBuilder = variableBuilder;
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {
        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);

        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(dependency, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildDependentVariableBuilderWrapper(contextInjectionNode, variableBuilder, returnType));

        return injectionNode;
    }
}
