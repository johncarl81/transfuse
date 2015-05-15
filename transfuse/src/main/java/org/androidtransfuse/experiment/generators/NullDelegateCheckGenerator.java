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
import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.model.MethodDescriptor;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class NullDelegateCheckGenerator implements Generation {

    @Factory
    public interface NullDelegateCheckGeneratorFactory {
        NullDelegateCheckGenerator build(ASTMethod method);
    }

    private final ASTMethod method;

    @Inject
    public NullDelegateCheckGenerator(ASTMethod method) {
        this.method = method;
    }

    @Override
    public String getName() {
        return "Null Delegate Check for " + method;
    }

    @Override
    public void schedule(final ComponentBuilder builder, final ComponentDescriptor descriptor) {
        builder.addLazy(method, GenerationPhase.SUPER, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                JExpression delegateExpression = builder.getExpressionMap().get(descriptor.getRootInjectionNode()).getExpression();
                block._if(delegateExpression.eq(JExpr._null()))._then()._return();
            }
        });
    }
}

