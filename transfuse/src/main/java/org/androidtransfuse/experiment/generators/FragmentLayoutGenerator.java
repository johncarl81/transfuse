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

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FragmentLayoutGenerator implements Generation {

    private final RResourceReferenceBuilder rResourceReferenceBuilder;
    private final ASTElementFactory astElementFactory;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;
    private final InjectionBindingBuilder injectionBindingBuilder;

    @Inject
    public FragmentLayoutGenerator(RResourceReferenceBuilder rResourceReferenceBuilder,
                                   ASTElementFactory astElementFactory,
                                   UniqueVariableNamer namer,
                                   ClassGenerationUtil generationUtil,
                                   InjectionBindingBuilder injectionBindingBuilder) {
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.astElementFactory = astElementFactory;
        this.namer = namer;
        this.generationUtil = generationUtil;
        this.injectionBindingBuilder = injectionBindingBuilder;
    }

    @Override
    public String getName() {
        return "Fragment Layout Generator";
    }

    @Override
    public void schedule(final ComponentBuilder builder, final ComponentDescriptor descriptor) {

        final ASTMethod onCreateViewMethod = astElementFactory.findMethod(AndroidLiterals.FRAGMENT, "onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE);
        builder.add(onCreateViewMethod, GenerationPhase.POSTSCOPES, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                ASTType target = descriptor.getTarget();
                final JVar viewDeclaration = builder.getDefinedClass().field(JMod.PRIVATE, generationUtil.ref(AndroidLiterals.VIEW), namer.generateName(AndroidLiterals.VIEW));
                builder.getAnalysisContext().getInjectionNodeBuilders().putType(AndroidLiterals.VIEW, injectionBindingBuilder.buildExpression(new TypedExpression(AndroidLiterals.VIEW, viewDeclaration)));

                JBlock isNullconditionalBlock = block._if(viewDeclaration.eq(JExpr._null()))._then();

                methodDescriptor.pushBody(isNullconditionalBlock);

                if (target.isAnnotated(Layout.class)) {
                    ASTAnnotation layoutAnnotation = target.getASTAnnotation(Layout.class);

                    Integer layout = layoutAnnotation == null ? null : layoutAnnotation.getProperty("value", Integer.class);

                    isNullconditionalBlock.assign(viewDeclaration, methodDescriptor.getExpression(AndroidLiterals.LAYOUT_INFLATER).getExpression()
                            .invoke("inflate")
                            .arg(rResourceReferenceBuilder.buildReference(layout))
                            .arg(methodDescriptor.getExpression(AndroidLiterals.VIEW_GROUP).getExpression())
                            .arg(JExpr.lit(false)));
                } else {
                    JInvocation onCreateView = JExpr._super().invoke("onCreateView");

                    for (ASTParameter astParameter : methodDescriptor.getASTMethod().getParameters()) {
                        onCreateView.arg(methodDescriptor.getExpression(astParameter.getASTType()).getExpression());
                    }

                    isNullconditionalBlock.assign(viewDeclaration, onCreateView);
                }

                builder.add(onCreateViewMethod, GenerationPhase.RETURN, new ComponentMethodGenerator() {
                    @Override
                    public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                        methodDescriptor.popBody();
                        methodDescriptor.getBody()._return(viewDeclaration);
                    }
                });
            }
        });
    }
}
