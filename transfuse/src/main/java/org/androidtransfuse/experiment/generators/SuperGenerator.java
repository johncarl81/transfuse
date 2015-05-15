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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.MethodSignature;
import org.androidtransfuse.analysis.astAnalyzer.ManualSuperAspect;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class SuperGenerator implements Generation {

    public
    @Factory
    interface SuperGeneratorFactory {
        SuperGenerator build(ASTMethod method);
    }

    private final ASTMethod method;

    @Inject
    public SuperGenerator(ASTMethod method) {
        this.method = method;
    }

    @Override
    public String getName() {
        return "Super Generator for " + method;
    }

    @Override
    public void schedule(final ComponentBuilder builder, ComponentDescriptor descriptor) {
        builder.addLazy(method, GenerationPhase.SUPER, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                if(!isSuperCanceled(builder.getExpressionMap().keySet())) {
                    JInvocation invocation = block.invoke(JExpr._super(), method.getName());

                    List<ASTParameter> parameters = methodDescriptor.getASTMethod().getParameters();

                    for (ASTParameter parameter : parameters) {
                        invocation.arg(methodDescriptor.getParameter(parameter).getExpression());
                    }
                }
            }
        });
    }

    private boolean isSuperCanceled(Set<InjectionNode> injectionNodes){

        MethodSignature signature = new MethodSignature(method);
        for (InjectionNode injectionNode : injectionNodes) {
            if(injectionNode.containsAspect(ManualSuperAspect.class)){
                ManualSuperAspect aspect = injectionNode.getAspect(ManualSuperAspect.class);

                for (ManualSuperAspect.Method manualSuperMethod : aspect.getMethods()) {
                    MethodSignature manualSuperMethodSignature = new MethodSignature(manualSuperMethod.getName(), manualSuperMethod.getParameters());
                    if(signature.equals(manualSuperMethodSignature)){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
