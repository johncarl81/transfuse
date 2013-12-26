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
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.MethodDescriptorBuilder;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class OnReceiveMethodBuilder implements MethodBuilder {

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;
    private final ASTClassFactory astClassFactory;
    private final ASTElementFactory astElementFactory;
    private final UniqueVariableNamer namer;

    @Inject
    public OnReceiveMethodBuilder(JCodeModel codeModel, ClassGenerationUtil generationUtil, ASTClassFactory astClassFactory, ASTElementFactory astElementFactory, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
        this.astClassFactory = astClassFactory;
        this.astElementFactory = astElementFactory;
        this.namer = namer;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        JMethod onReceiveMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onReceive");
        onReceiveMethod.annotate(Override.class);
        ASTMethod onReceiveASTMethod = astElementFactory.findMethod(AndroidLiterals.BROADCAST_RECEIVER, "onReceive", AndroidLiterals.CONTEXT, AndroidLiterals.INTENT);

        MethodDescriptorBuilder methodDescriptor = new MethodDescriptorBuilder(onReceiveMethod, onReceiveASTMethod);

        for (ASTParameter astParameter : onReceiveASTMethod.getParameters()) {
            JVar param = onReceiveMethod.param(generationUtil.ref(astParameter.getASTType()), namer.generateName(astParameter.getASTType()));
            methodDescriptor.putParameter(astParameter, new TypedExpression(astParameter.getASTType(), param));
        }

        return methodDescriptor.build();
    }

    @Override
    public void closeMethod(MethodDescriptor descriptor) {
        //no close necessary
    }
}
