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
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.annotations.FragmentLayoutHandler;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.InstantiationStrategyFactory;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.layout.FragmentLayoutHandlerDelegate;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class FragmentLayoutGenerator implements Generation {

    private final RResourceReferenceBuilder rResourceReferenceBuilder;
    private final ASTElementFactory astElementFactory;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;
    private final InjectionPointFactory injectionPointFactory;
    private final Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider;
    private final Validator validator;

    @Inject
    public FragmentLayoutGenerator(RResourceReferenceBuilder rResourceReferenceBuilder,
                                   ASTElementFactory astElementFactory,
                                   UniqueVariableNamer namer,
                                   ClassGenerationUtil generationUtil,
                                   InjectionBindingBuilder injectionBindingBuilder,
                                   InjectionFragmentGenerator injectionFragmentGenerator,
                                   InstantiationStrategyFactory instantiationStrategyFactory,
                                   InjectionPointFactory injectionPointFactory,
                                   Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider,
                                   Validator validator) {
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.astElementFactory = astElementFactory;
        this.namer = namer;
        this.generationUtil = generationUtil;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.astTypeBuilderVisitorProvider = astTypeBuilderVisitorProvider;
        this.validator = validator;
    }

    @Override
    public String getName() {
        return "Fragment Layout Generator";
    }

    @Override
    public void schedule(final ComponentBuilder builder, final ComponentDescriptor descriptor) {

        final InjectionNode layoutHandlerInjectionNode;
        ASTType target = descriptor.getTarget();

        if(target.isAnnotated(FragmentLayoutHandler.class)) {
            FragmentLayoutHandler layoutHandlerAnnotation = target.getAnnotation(FragmentLayoutHandler.class);
            layoutHandlerInjectionNode = buildLayoutHandlerInjectionNode(layoutHandlerAnnotation, builder.getAnalysisContext());
        }
        else {
            layoutHandlerInjectionNode = null;
        }

        final ASTMethod onCreateViewMethod = astElementFactory.findMethod(AndroidLiterals.FRAGMENT, "onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE);
        builder.add(onCreateViewMethod, GenerationPhase.POSTSCOPES, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                ASTType target = descriptor.getTarget();
                final JVar viewDeclaration = builder.getDefinedClass().field(JMod.PRIVATE, generationUtil.ref(AndroidLiterals.VIEW), namer.generateName(AndroidLiterals.VIEW));
                builder.getAnalysisContext().getInjectionNodeBuilders().putType(AndroidLiterals.VIEW, injectionBindingBuilder.buildExpression(new TypedExpression(AndroidLiterals.VIEW, viewDeclaration)));

                JBlock isNullconditionalBlock = block._if(viewDeclaration.eq(JExpr._null()))._then();

                methodDescriptor.pushBody(isNullconditionalBlock);

                boolean handled = false;

                if (target.isAnnotated(Layout.class)) {
                    ASTAnnotation layoutAnnotation = target.getASTAnnotation(Layout.class);

                    Integer layout = layoutAnnotation == null ? null : layoutAnnotation.getProperty("value", Integer.class);

                    isNullconditionalBlock.assign(viewDeclaration, methodDescriptor.getExpression(AndroidLiterals.LAYOUT_INFLATER).getExpression()
                            .invoke("inflate")
                            .arg(rResourceReferenceBuilder.buildReference(layout))
                            .arg(methodDescriptor.getExpression(AndroidLiterals.VIEW_GROUP).getExpression())
                            .arg(JExpr.lit(false)));

                    handled = true;
                }

                if (layoutHandlerInjectionNode != null) {
                    if(handled){
                        validator.error("Layout is already defined with @Layout")
                                .element(target)
                                .annotation(target.getASTAnnotation(FragmentLayoutHandler.class))
                                .build();
                    }
                    try {
                        Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(isNullconditionalBlock,
                                instantiationStrategyFactory.buildMethodStrategy(isNullconditionalBlock, builder.getScopes()),
                                builder.getDefinedClass(),
                                layoutHandlerInjectionNode,
                                builder.getScopes());

                        //FragmentLayoutHandlerDelegate.invokeLayout(layoutInflater, viewGroup)
                        JExpression layoutHandlerDelegate = expressionMap.get(layoutHandlerInjectionNode).getExpression();

                        isNullconditionalBlock.invoke(layoutHandlerDelegate, FragmentLayoutHandlerDelegate.INVOKE_LAYOUT_METHOD)
                            .arg(methodDescriptor.getParameter(methodDescriptor.getASTMethod().getParameters().get(0)).getExpression())
                            .arg(methodDescriptor.getParameter(methodDescriptor.getASTMethod().getParameters().get(1)).getExpression());


                    } catch (JClassAlreadyExistsException e) {
                        throw new TransfuseAnalysisException("Class Already Exists ", e);
                    }


                    handled = true;
                }

                if (!handled) {
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

    private InjectionNode buildLayoutHandlerInjectionNode(final FragmentLayoutHandler layoutHandlerAnnotation, AnalysisContext context) {
        if (layoutHandlerAnnotation != null) {
            TypeMirror layoutHandlerType = getTypeMirror(layoutHandlerAnnotation, "value");

            if (layoutHandlerType != null) {
                ASTType layoutHandlerASTType = layoutHandlerType.accept(astTypeBuilderVisitorProvider.get(), null);
                return injectionPointFactory.buildInjectionNode(layoutHandlerASTType, context);
            }
        }
        return null;
    }
}
