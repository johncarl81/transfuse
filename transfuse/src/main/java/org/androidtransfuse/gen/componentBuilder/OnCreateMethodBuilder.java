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
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class OnCreateMethodBuilder implements MethodBuilder {

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;
    private final ASTMethod onCreateASTMethod;
    private final UniqueVariableNamer namer;
    private final LayoutBuilder layoutBuilder;

    @Inject
    public OnCreateMethodBuilder(/*@Assisted*/ ASTMethod onCreateASTMethod, /*@Assisted*/ LayoutBuilder layoutBuilder, JCodeModel codeModel, ClassGenerationUtil generationUtil, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.onCreateASTMethod = onCreateASTMethod;
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.layoutBuilder = layoutBuilder;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
        onCreateMethod.annotate(Override.class);
        MethodDescriptorBuilder onCreateMethodDescriptorBuilder = new MethodDescriptorBuilder(onCreateMethod, onCreateASTMethod);

        List<JVar> parameters = new ArrayList<JVar>();

        for (ASTParameter methodArgument : onCreateASTMethod.getParameters()) {
            JVar param = onCreateMethod.param(generationUtil.ref(methodArgument.getASTType()), namer.generateName(methodArgument.getASTType()));
            parameters.add(param);
            onCreateMethodDescriptorBuilder.putParameter(methodArgument, new TypedExpression(methodArgument.getASTType(), param));
        }

        //super.onCreate()
        JBlock block = onCreateMethod.body();
        JInvocation invocation = block.invoke(JExpr._super(), onCreateMethod);

        for (JVar parameter : parameters) {
            invocation.arg(parameter);
        }

        layoutBuilder.buildLayoutCall(definedClass, block);

        return onCreateMethodDescriptorBuilder.build();
    }

    @Override
    public void closeMethod(MethodDescriptor descriptor) {
        //no close necessary
    }
}
