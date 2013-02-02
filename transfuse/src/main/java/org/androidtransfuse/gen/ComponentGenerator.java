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
import org.androidtransfuse.scope.ScopesUtil;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentGenerator  {

    private final JCodeModel codeModel;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public ComponentGenerator(JCodeModel codeModel,
                              InjectionFragmentGenerator injectionFragmentGenerator,
                              ComponentBuilderFactory componentBuilderFactory,
                              ClassGenerationUtil generationUtil) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.componentBuilderFactory = componentBuilderFactory;
        this.generationUtil = generationUtil;
    }

    public JDefinedClass generate(ComponentDescriptor descriptor) {
        if (descriptor == null) {
            return null;
        }

        try {
            final JDefinedClass definedClass = generationUtil.defineClass(descriptor.getPackageClass());

            definedClass._extends(codeModel.ref(descriptor.getType()));

            MethodDescriptor initMethodDescriptor = descriptor.getInitMethodBuilder().buildMethod(definedClass);

            JBlock block = initMethodDescriptor.getMethod().body();

            //Singleton scopes holder
            JExpression scopesGetInstance = codeModel.ref(ScopesUtil.class).staticInvoke(ScopesUtil.GET_INSTANCE);

            //Injections
            Map<InjectionNode, TypedExpression> expressionMap =
                    injectionFragmentGenerator.buildFragment(
                            block,
                            definedClass,
                            descriptor.getInjectionNodeFactory().buildInjectionNode(initMethodDescriptor),
                            scopesGetInstance);

            //Registrations
            for (ExpressionVariableDependentGenerator registrationGenerator : descriptor.getRegistrations()) {
                registrationGenerator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor, scopesGetInstance);
            }

            //Method Callbacks
            MethodGenerator onCreateMethodGenerator = new ExistingMethod(initMethodDescriptor);
            MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator(
                    descriptor.getInitMethodEventAnnotation(), onCreateMethodGenerator);

            onCreateCallbackGenerator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor, scopesGetInstance);

            //... and other listeners
            for (ExpressionVariableDependentGenerator generator : descriptor.getGenerators()) {
                generator.generate(definedClass, initMethodDescriptor, expressionMap, descriptor, scopesGetInstance);
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
