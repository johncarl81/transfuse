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
package org.androidtransfuse.gen.componentBuilder;

import com.google.common.collect.ImmutableList;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTVoidType;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentMethodGenerator;
import org.androidtransfuse.experiment.GenerationPhase;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ActivityDelegateRegistrationGenerator implements RegistrationGenerator {

    private final ImmutableList<ASTMethod> methods;
    private final ActivityDelegateASTReference activityDelegateASTReference;

    @Inject
    public ActivityDelegateRegistrationGenerator(/*@Assisted*/ ActivityDelegateASTReference activityDelegateASTReference,
                                                 /*@Assisted*/ ImmutableList<ASTMethod> methods) {
        this.methods = methods;
        this.activityDelegateASTReference = activityDelegateASTReference;
    }

    @Override
    public void build(final ComponentBuilder componentBuilder, ASTMethod creationMethod, final TypedExpression value) {
        for (ASTMethod method : methods) {

            componentBuilder.add(method, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
                @Override
                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                    JExpression targetExpression = activityDelegateASTReference.buildReference(componentBuilder.getDefinedClass(), value);

                    JInvocation delegateInvocation = targetExpression.invoke(methodDescriptor.getASTMethod().getName());

                    for (ASTParameter astParameter : methodDescriptor.getASTMethod().getParameters()) {
                        delegateInvocation.arg(methodDescriptor.getParameter(astParameter).getExpression());
                    }

                    if(ASTVoidType.VOID.equals(methodDescriptor.getASTMethod().getReturnType())){
                        block.add(delegateInvocation);
                    }
                    else{
                        block._return(delegateInvocation);
                    }
                }
            });
        }
    }
}
