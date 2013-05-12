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
package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.Factories;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.AnalysisContextFactory;
import org.androidtransfuse.analysis.module.ModuleRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.componentBuilder.InjectionNodeFactory;
import org.androidtransfuse.gen.componentBuilder.InjectionNodeImplFactory;
import org.androidtransfuse.gen.componentBuilder.MirroredMethodGenerator;
import org.androidtransfuse.gen.componentBuilder.MirroredMethodGeneratorFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class FactoryGenerator {

    private final JCodeModel codeModel;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InjectionNodeImplFactory injectionNodeImplFactory;
    private final MirroredMethodGeneratorFactory mirroredMethodGeneratorFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final ClassGenerationUtil generationUtil;
    private final ModuleRepository injectionNodeBuilderRepositoryFactory;
    private final UniqueVariableNamer namer;
    private final Validator validator;

    @Inject
    public FactoryGenerator(JCodeModel codeModel,
                            InjectionFragmentGenerator injectionFragmentGenerator,
                            AnalysisContextFactory analysisContextFactory,
                            Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                            ModuleRepository injectionNodeBuilderRepositoryFactory,
                            InjectionNodeImplFactory injectionNodeImplFactory,
                            MirroredMethodGeneratorFactory mirroredMethodGeneratorFactory,
                            ClassGenerationUtil generationUtil,
                            UniqueVariableNamer namer,
                            Validator validator) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.injectionNodeImplFactory = injectionNodeImplFactory;
        this.mirroredMethodGeneratorFactory = mirroredMethodGeneratorFactory;
        this.generationUtil = generationUtil;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.namer = namer;
        this.validator = validator;
    }

    public JDefinedClass generate(ASTType descriptor) {

        if (descriptor.isConcreteClass()) {
            validator.error("@Factory annotated class must be an interface")
                                           .element(descriptor).build();
            throw new TransfuseAnalysisException("Unable to build factory from concrete class: " + descriptor.getName());
        }

        try {
            JDefinedClass implClass = generationUtil.defineClass(descriptor.getPackageClass().append(Factories.IMPL_EXT));
            JClass interfaceClass = codeModel.ref(descriptor.getName());

            //scope holder definition
            JFieldVar scopesField = implClass.field(JMod.PRIVATE, Scopes.class, namer.generateName(Scopes.class));

            JMethod constructor = implClass.constructor(JMod.PUBLIC);
            JVar scopesParam = constructor.param(Scopes.class, namer.generateName(Scopes.class));

            constructor.body().assign(scopesField, scopesParam);

            //default constructor todo: use non-default to propogate scopes
            JMethod defaultConstructor = implClass.constructor(JMod.PUBLIC);

            defaultConstructor.body().assign(scopesField, JExpr._new(codeModel.ref(Scopes.class)));

            implClass._implements(interfaceClass);

            for (ASTMethod interfaceMethod : descriptor.getMethods()) {
                MirroredMethodGenerator mirroredMethodGenerator = mirroredMethodGeneratorFactory.buildMirroredMethodGenerator(interfaceMethod, false);
                MethodDescriptor methodDescriptor = mirroredMethodGenerator.buildMethod(implClass);
                JBlock block = methodDescriptor.getMethod().body();

                InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();
                injectionNodeBuilderRepository.addRepository(injectionNodeBuilderRepositoryFactory.buildModuleConfiguration());
                AnalysisContext context = analysisContextFactory.buildAnalysisContext(injectionNodeBuilderRepository);
                InjectionNodeFactory injectionNodeFactory = injectionNodeImplFactory.buildInjectionNodeFactory(interfaceMethod.getReturnType(), context);

                //Injections
                InjectionNode returnType = injectionNodeFactory.buildInjectionNode(methodDescriptor);
                Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, implClass, returnType, scopesField);

                block._return(expressionMap.get(returnType).getExpression());

            }

            return implClass;

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class already exists for generated type " + descriptor.getName(), e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Target class not found", e);
        }
    }
}
