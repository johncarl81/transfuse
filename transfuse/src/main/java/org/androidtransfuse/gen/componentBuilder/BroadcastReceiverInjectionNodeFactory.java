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
import org.androidtransfuse.analysis.AnalysisContextFactory;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverInjectionNodeFactory implements InjectionNodeFactory {

    private final AnalysisContextFactory analysisContextFactory;
    private final InjectionPointFactory injectionPointFactory;
    private final InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private final InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private final ASTType astType;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;

    @Inject
    public BroadcastReceiverInjectionNodeFactory(@Assisted ASTType astType,
                                                 AnalysisContextFactory analysisContextFactory,
                                                 InjectionPointFactory injectionPointFactory,
                                                 InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                                                 InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                                                 InjectionBindingBuilder injectionBindingBuilder, InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory) {
        this.analysisContextFactory = analysisContextFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
        this.astType = astType;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor) {
        AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(onCreateMethodDescriptor));
        return injectionPointFactory.buildInjectionNode(astType, context);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(MethodDescriptor methodDescriptor) {

        injectionNodeBuilderRepository.putType(android.content.BroadcastReceiver.class, injectionBindingBuilder.buildThis(android.content.BroadcastReceiver.class));

        for (Map.Entry<ASTParameter, TypedExpression> parameterEntry : methodDescriptor.getParameters().entrySet()) {
            injectionNodeBuilderRepository.putType(parameterEntry.getKey().getASTType(), injectionBindingBuilder.buildExpression(parameterEntry.getValue()));
        }

        injectionNodeBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        injectionNodeBuilderRepositoryFactory.addModuleConfiguration(injectionNodeBuilderRepository);

        variableBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        return injectionNodeBuilderRepository;
    }
}
