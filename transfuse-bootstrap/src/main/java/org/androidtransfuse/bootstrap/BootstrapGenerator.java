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
package org.androidtransfuse.bootstrap;

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.analysis.module.ModuleRepository;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.scope.Scopes;

/**
 * @author John Ericksen
 */
public class BootstrapGenerator {

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer variableNamer;
    private final InjectionFragmentGenerator injectionGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;
    private final ExistingVariableInjectionBuilderFactory variableBuilderFactory;
    private final ModuleRepository repository;

    public BootstrapGenerator(JCodeModel codeModel,
                              ClassGenerationUtil generationUtil,
                              UniqueVariableNamer variableNamer,
                              InjectionFragmentGenerator injectionGenerator,
                              InstantiationStrategyFactory instantiationStrategyFactory,
                              ExistingVariableInjectionBuilderFactory variableBuilderFactory,
                              ModuleRepository repository) {
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
        this.variableNamer = variableNamer;
        this.injectionGenerator = injectionGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
        this.variableBuilderFactory = variableBuilderFactory;
        this.repository = repository;
    }

    public JDefinedClass generate(InjectionNode injectionNode){

        try {
            JClass nodeClass = generationUtil.ref(injectionNode.getASTType());

            // add injector class
            PackageClass bootstrapClassName = ClassNamer.className(injectionNode)
                    .append(Bootstraps.IMPL_EXT)
                    .build();

            JDefinedClass innerInjectorClass = generationUtil.defineClass(bootstrapClassName);

            innerInjectorClass._extends(generationUtil.ref(Bootstraps.BootstrapsInjectorAdapter.class).narrow(nodeClass));

            JMethod method = innerInjectorClass.method(JMod.PUBLIC, codeModel.VOID, Bootstraps.BOOTSTRAPS_INJECTOR_METHOD);
            JVar input = method.param(nodeClass, variableNamer.generateName(nodeClass));
            JBlock injectorBlock = method.body();

            //define root scope holder
            JVar scopesVar = injectorBlock.decl(generationUtil.ref(Scopes.class), variableNamer.generateName(Scopes.class), ScopesGenerator.buildScopes(repository, generationUtil));

            injectorBlock.add(JExpr.invoke(Bootstraps.BootstrapsInjectorAdapter.SCOPE_SINGLETONS_METHOD).arg(scopesVar));

            //Setup injection aspect
            injectionNode.addAspect(VariableBuilder.class, variableBuilderFactory.buildVariableBuilder(input));

            injectionGenerator.buildFragment(injectorBlock,
                    instantiationStrategyFactory.buildMethodStrategy(injectorBlock, scopesVar),
                    innerInjectorClass,
                    injectionNode,
                    scopesVar);

            // add instance to map
            return innerInjectorClass;

        } catch (JClassAlreadyExistsException e) {
            throw new BootstrapException("Unable to crate Bootstrap Factory, class already exists.", e);
        }
    }
}
