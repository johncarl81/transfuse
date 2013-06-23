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
package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.MethodDescriptorBuilder;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

public class MirroredMethodGenerator implements MethodGenerator {
    private final ASTMethod overrideMethod;
    private final boolean superCall;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer variableNamer;

    @Inject
    public MirroredMethodGenerator(/*@Assisted*/ ASTMethod overrideMethod, /*@Assisted*/ boolean superCall, ClassGenerationUtil generationUtil, UniqueVariableNamer variableNamer) {
        this.overrideMethod = overrideMethod;
        this.superCall = superCall;
        this.generationUtil = generationUtil;
        this.variableNamer = variableNamer;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        JMethod method = definedClass.method(JMod.PUBLIC, generationUtil.ref(overrideMethod.getReturnType()), overrideMethod.getName());
        method.annotate(Override.class);

        MethodDescriptorBuilder methodDescriptorBuilder = new MethodDescriptorBuilder(method, overrideMethod);

        //parameters
        for (ASTParameter astParameter : overrideMethod.getParameters()) {
            JVar param = method.param(generationUtil.ref(astParameter.getASTType()), variableNamer.generateName(astParameter.getASTType()));
            methodDescriptorBuilder.putParameter(astParameter, new TypedExpression(astParameter.getASTType(), param));
        }

        MethodDescriptor methodDescriptor = methodDescriptorBuilder.build();

        if (superCall) {
            JBlock body = method.body();
            JInvocation superInvocation = JExpr._super().invoke(overrideMethod.getName());
            body.add(superInvocation);

            for (ASTParameter astParameter : overrideMethod.getParameters()) {
                superInvocation.arg(methodDescriptor.getParameter(astParameter).getExpression());
            }
        }
        return methodDescriptor;
    }

    @Override
    public void closeMethod(MethodDescriptor methodDescriptor) {
        //noop
    }
}