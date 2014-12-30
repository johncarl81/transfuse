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
package org.androidtransfuse.analysis.astAnalyzer.registration;

import com.google.common.collect.ImmutableList;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.analysis.repository.RegistrationGeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.ActivityDelegateASTReference;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.RegistrationGenerator;
import org.androidtransfuse.listeners.CallThrough;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

public class ActivityDelegateRegistrationGeneratorFactory implements RegistrationGeneratorFactory {

    private final ImmutableList<ASTMethod> methods;
    private final ComponentBuilderFactory componentBuilderFactory;

    @Inject
    public ActivityDelegateRegistrationGeneratorFactory(/*@Assisted*/ ASTType listenerInterface, ComponentBuilderFactory componentBuilderFactory) {
        this.componentBuilderFactory = componentBuilderFactory;

        ImmutableList.Builder<ASTMethod> delegateMethods = ImmutableList.builder();
        for (ASTMethod method : listenerInterface.getMethods()) {
            if (method.isAnnotated(CallThrough.class)) {
                delegateMethods.add(method);
            }
        }

        this.methods = delegateMethods.build();
    }

    @Override
    public RegistrationGenerator buildRegistrationGenerator(InjectionNode injectionNode, ASTBase astBase, ASTAnnotation registerAnnotation, AnalysisContext context) {

        ActivityDelegateASTReference delegateASTReference;

        if (astBase instanceof ASTType) {
            delegateASTReference = componentBuilderFactory.buildActivityTypeDelegateASTReference();
        } else if (astBase instanceof ASTMethod) {
            delegateASTReference = componentBuilderFactory.buildActivityMethodDelegateASTReference((ASTMethod) astBase);
        } else if (astBase instanceof ASTField) {
            delegateASTReference = componentBuilderFactory.buildActivityFieldDelegateASTReference((ASTField) astBase);
        } else {
            throw new TransfuseAnalysisException("ASTBase type not mapped");
        }

        //set injection node to field
        if (!injectionNode.containsAspect(ASTInjectionAspect.class)) {
            injectionNode.addAspect(new ASTInjectionAspect());
        }
        injectionNode.getAspect(ASTInjectionAspect.class).setAssignmentType(ASTInjectionAspect.InjectionAssignmentType.FIELD);

        return componentBuilderFactory.buildActivityRegistrationGenerator(delegateASTReference, methods);
    }
}