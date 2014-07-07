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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ComponentMethodGenerator;
import org.androidtransfuse.experiment.Generation;
import org.androidtransfuse.model.MethodDescriptor;

import javax.inject.Inject;
import java.util.List;

/**
 * @author John Ericksen
 */
public class SuperGenerator implements Generation, ComponentMethodGenerator {

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
    public void schedule(ComponentBuilder builder, ComponentDescriptor descriptor) {
        //builder.add(method, GenerationPhase.SUPER, this);
        builder.addLazy(method, this);
    }

    @Override
    public void generate(MethodDescriptor methodDescriptor, JBlock block) {
        JInvocation invocation = block.invoke(JExpr._super(), method.getName());

        List<ASTParameter> parameters = methodDescriptor.getASTMethod().getParameters();

        for (ASTParameter parameter : parameters) {
            invocation.arg(methodDescriptor.getParameter(parameter).getExpression());
        }
    }
}
