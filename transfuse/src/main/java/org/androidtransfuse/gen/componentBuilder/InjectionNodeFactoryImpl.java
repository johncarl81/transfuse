/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNodeFactoryImpl implements InjectionNodeFactory {

    private final ASTType astType;
    private final AnalysisContext context;
    private final InjectionPointFactory injectionPointFactory;
    private final InjectionBindingBuilder injectionBindingBuilder;

    @Inject
    public InjectionNodeFactoryImpl(@Assisted ASTType astType,
                                    @Assisted AnalysisContext context,
                                    InjectionPointFactory injectionPointFactory,
                                    InjectionBindingBuilder injectionBindingBuilder) {
        this.astType = astType;
        this.context = context;
        this.injectionPointFactory = injectionPointFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
    }

    @Override
    public InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor) {
        buildVariableBuilderMap(onCreateMethodDescriptor, context.getInjectionNodeBuilders());

        return injectionPointFactory.buildInjectionNode(astType, context);
    }

    private void buildVariableBuilderMap(MethodDescriptor methodDescriptor, InjectionNodeBuilderRepository injectionNodeBuilders) {

        for (Map.Entry<ASTType, TypedExpression> parameterEntry : methodDescriptor.getTypeMap().entrySet()) {
            injectionNodeBuilders.putType(parameterEntry.getKey(), injectionBindingBuilder.buildExpression(parameterEntry.getValue()));
        }
    }
}
