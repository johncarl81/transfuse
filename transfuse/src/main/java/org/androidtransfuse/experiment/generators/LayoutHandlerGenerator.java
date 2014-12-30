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
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.annotations.LayoutHandler;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.layout.LayoutHandlerDelegate;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class LayoutHandlerGenerator implements Generation {

    private final Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider;
    private final InjectionPointFactory injectionPointFactory;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;
    private final ASTElementFactory astElementFactory;

    @Inject
    public LayoutHandlerGenerator(Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider,
                                  InjectionPointFactory injectionPointFactory,
                                  InjectionFragmentGenerator injectionFragmentGenerator,
                                  InstantiationStrategyFactory instantiationStrategyFactory,
                                  UniqueVariableNamer namer,
                                  ClassGenerationUtil generationUtil,
                                  ASTElementFactory astElementFactory) {
        this.astTypeBuilderVisitorProvider = astTypeBuilderVisitorProvider;
        this.injectionPointFactory = injectionPointFactory;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
        this.namer = namer;
        this.generationUtil = generationUtil;
        this.astElementFactory = astElementFactory;
    }

    @Override
    public void schedule(final ComponentBuilder builder, final ComponentDescriptor descriptor) {

        ASTType target = descriptor.getTarget();

        if(target.isAnnotated(LayoutHandler.class)) {
            LayoutHandler layoutHandlerAnnotation = target.getAnnotation(LayoutHandler.class);

            final InjectionNode layoutHandlerInjectionNode = buildLayoutHandlerInjectionNode(layoutHandlerAnnotation, builder.getAnalysisContext());

            ASTMethod onCreateMethod = astElementFactory.findMethod(AndroidLiterals.ACTIVITY, "onCreate", AndroidLiterals.BUNDLE);
            builder.add(onCreateMethod, GenerationPhase.LAYOUT, new ComponentMethodGenerator() {
                @Override
                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                    try {
                        JDefinedClass definedClass = builder.getDefinedClass();

                        // Scopes instance
                        JClass scopesRef = generationUtil.ref(Scopes.class);
                        JInvocation scopesBuildInvocation = generationUtil.ref(ScopesGenerator.TRANSFUSE_SCOPES_UTIL).staticInvoke(ScopesGenerator.GET_INSTANCE);
                        JVar scopesVar = block.decl(scopesRef, namer.generateName(Scopes.class), scopesBuildInvocation);

                        Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block,
                                instantiationStrategyFactory.buildMethodStrategy(block, scopesVar),
                                definedClass,
                                layoutHandlerInjectionNode,
                                scopesVar);

                        //LayoutHandlerDelegate.invokeLayout()
                        JExpression layoutHandlerDelegate = expressionMap.get(layoutHandlerInjectionNode).getExpression();

                        block.add(layoutHandlerDelegate.invoke(LayoutHandlerDelegate.INVOKE_LAYOUT_METHOD));

                    } catch (JClassAlreadyExistsException e) {
                        throw new TransfuseAnalysisException("Class Already Exists ", e);
                    }
                }
            });
        }


    }

    private InjectionNode buildLayoutHandlerInjectionNode(final LayoutHandler layoutHandlerAnnotation, AnalysisContext context) {
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
