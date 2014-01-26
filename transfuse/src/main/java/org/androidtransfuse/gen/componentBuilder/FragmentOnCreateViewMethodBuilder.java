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
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FragmentOnCreateViewMethodBuilder implements MethodBuilder {

    private final ClassGenerationUtil generationUtil;
    private final ASTMethod onCreateViewMethod;
    private final UniqueVariableNamer namer;
    private final Integer layout;
    private final RResourceReferenceBuilder rResourceReferenceBuilder;

    @Inject
    public FragmentOnCreateViewMethodBuilder(/*@Assisted*/ /*@Nullable*/ Integer layout,
                                             /*@Assisted*/ ASTMethod onCreateViewMethod,
                                             ClassGenerationUtil generationUtil,
                                             UniqueVariableNamer namer,
                                             RResourceReferenceBuilder rResourceReferenceBuilder) {
        this.onCreateViewMethod = onCreateViewMethod;
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.layout = layout;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, generationUtil.ref(AndroidLiterals.VIEW), "onCreateView");
        onCreateMethod.annotate(Override.class);
        MethodDescriptorBuilder onCreateMethodDescriptorBuilder = new MethodDescriptorBuilder(onCreateMethod, onCreateViewMethod);

        for (ASTParameter methodArgument : onCreateViewMethod.getParameters()) {
            JVar param = onCreateMethod.param(generationUtil.ref(methodArgument.getASTType()), namer.generateName(methodArgument.getASTType()));
            onCreateMethodDescriptorBuilder.putParameter(methodArgument, new TypedExpression(methodArgument.getASTType(), param));
        }

        //layoutInflater_0 .inflate(layout.details, viewGroup_0, false);
        JBlock body = onCreateMethod.body();

        JVar viewDeclaration = body.decl(generationUtil.ref(AndroidLiterals.VIEW), namer.generateName(AndroidLiterals.VIEW));

        onCreateMethodDescriptorBuilder.putType(AndroidLiterals.VIEW, new TypedExpression(AndroidLiterals.VIEW, viewDeclaration));

        MethodDescriptor onCreateMethodDescriptor = onCreateMethodDescriptorBuilder.build();

        if (layout == null) {
            JInvocation onCreateView = JExpr._super().invoke("onCreateView");

            for (ASTParameter astParameter : onCreateViewMethod.getParameters()) {
                onCreateView.arg(onCreateMethodDescriptor.getExpression(astParameter.getASTType()).getExpression());
            }

            body.assign(viewDeclaration, onCreateView);
        } else {
            body.assign(viewDeclaration, onCreateMethodDescriptor.getExpression(AndroidLiterals.LAYOUT_INFLATER).getExpression()
                    .invoke("inflate")
                    .arg(rResourceReferenceBuilder.buildReference(layout))
                    .arg(onCreateMethodDescriptor.getExpression(AndroidLiterals.VIEW_GROUP).getExpression())
                    .arg(JExpr.lit(false)));
        }

        return onCreateMethodDescriptor;
    }

    public void closeMethod(MethodDescriptor descriptor) {
        JMethod method = descriptor.getMethod();

        method.body()._return(descriptor.getExpression(AndroidLiterals.VIEW).getExpression());
    }
}
