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
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentGenerator  {

    private final JCodeModel codeModel;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;
    private final Logger logger;

    @Inject
    public ComponentGenerator(JCodeModel codeModel, Logger logger,
                              InjectionFragmentGenerator injectionFragmentGenerator,
                              InstantiationStrategyFactory instantiationStrategyFactory, ComponentBuilderFactory componentBuilderFactory,
                              ClassGenerationUtil generationUtil,
                              UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.logger = logger;
    }

    public JDefinedClass generate(ComponentDescriptor descriptor) {
        if (descriptor == null) {
            return null;
        }

        try {
            final JDefinedClass definedClass = generationUtil.defineClass(descriptor.getPackageClass());

            definedClass._extends(generationUtil.ref(descriptor.getType()));

            MethodDescriptor initMethodDescriptor = descriptor.getInitMethodBuilder().buildMethod(definedClass);

            JBlock block = initMethodDescriptor.getMethod().body();

            // Scopes instance
            JClass scopesRef = codeModel.ref(Scopes.class);
            JInvocation scopesBuildInvocation = codeModel.directClass(ScopesGenerator.TRANSFUSE_SCOPES_UTIL.getCanonicalName()).staticInvoke(ScopesGenerator.GET_INSTANCE);
            JVar scopesVar = block.decl(scopesRef, namer.generateName(Scopes.class), scopesBuildInvocation);

            //Injections
            Map<InjectionNode, TypedExpression> expressionMap;
            try {
              expressionMap = injectionFragmentGenerator.buildFragment(
                      block,
                      instantiationStrategyFactory.buildMethodStrategy(definedClass, block, scopesVar),
                      definedClass,
                      descriptor.getInjectionNodeFactory().buildInjectionNode(initMethodDescriptor),
                      scopesVar);
            } catch (RuntimeException e) {
              logger.info("unable to generate component (" + definedClass.name() + ") this pass");
              throw e;
            }
            logger.info("generated component: " + definedClass.name());

            //Registrations
            for (ExpressionVariableDependentGenerator registrationGenerator : descriptor.getRegistrations()) {
                registrationGenerator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor, scopesVar);
            }

            //Method Callbacks
            MethodGenerator onCreateMethodGenerator = new ExistingMethod(initMethodDescriptor);
            MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator(
                    descriptor.getInitMethodEventAnnotation(), onCreateMethodGenerator);

            onCreateCallbackGenerator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor, scopesVar);

            //... and other listeners
            for (ExpressionVariableDependentGenerator generator : descriptor.getGenerators()) {
                generator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor, scopesVar);
            }

            descriptor.getInitMethodBuilder().closeMethod(initMethodDescriptor);

            return definedClass;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class Already Exists ", e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Injection Fragment", e);
        }
    }
}
