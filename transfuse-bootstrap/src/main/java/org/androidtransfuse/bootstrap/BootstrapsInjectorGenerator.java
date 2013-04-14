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
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.ScopesGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class BootstrapsInjectorGenerator {

    public static final PackageClass BOOTSTRAPS_INJECTOR = new PackageClass(
            Bootstraps.BOOTSTRAPS_INJECTOR_PACKAGE, Bootstraps.BOOTSTRAPS_INJECTOR_NAME);

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;
    private final InjectionFragmentGenerator injectionGenerator;
    private final ExistingVariableInjectionBuilderFactory variableBuilderFactory;
    private final ModuleRepository repository;

    private JDefinedClass injectorClass = null;
    private JBlock registerBlock = null;
    private JFieldVar registerField = null;

    public BootstrapsInjectorGenerator(JCodeModel codeModel,
                                       ClassGenerationUtil generationUtil,
                                       UniqueVariableNamer namer,
                                       InjectionFragmentGenerator injectionGenerator,
                                       ExistingVariableInjectionBuilderFactory variableBuilderFactory,
                                       ModuleRepository repository) {
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.injectionGenerator = injectionGenerator;
        this.variableBuilderFactory = variableBuilderFactory;
        this.repository = repository;
    }

    public void generate(InjectionNode injectionNode){

        try {
            JClass nodeClass = codeModel.ref(injectionNode.getClassName());

            // add injector class
            JDefinedClass innerInjectorClass = generationUtil.defineClass(injectionNode.getASTType().getPackageClass().append(Bootstraps.IMPL_EXT));

            innerInjectorClass._extends(codeModel.ref(Bootstraps.BootstrapsInjectorAdapter.class).narrow(nodeClass));

            JMethod method = innerInjectorClass.method(JMod.PUBLIC, codeModel.VOID, Bootstraps.BOOTSTRAPS_INJECTOR_METHOD);
            JVar input = method.param(nodeClass, "input");
            JBlock injectorBlock = method.body();

            //define root scope holder
            JVar scopesVar = ScopesGenerator.buildScopes(repository, codeModel, namer, injectorBlock);

            injectorBlock.add(JExpr.invoke("scopeSingletons").arg(scopesVar));

            setupInjectionAspect(injectionNode, input);

            injectionGenerator.buildFragment(injectorBlock, innerInjectorClass, injectionNode, scopesVar);

            // add instance to map
            addBootstrapRegistration(nodeClass, innerInjectorClass);

        } catch (JClassAlreadyExistsException e) {
            throw new BootstrapException("Unable to crate Bootstrap Factory, class already exists.", e);
        } catch (ClassNotFoundException e) {
            throw new BootstrapException("Unable to find class", e);
        }
    }

    private void setupInjectionAspect(InjectionNode injectionNode, JVar input) {
        injectionNode.addAspect(VariableBuilder.class, variableBuilderFactory.buildVariableBuilder(input));
    }

    private synchronized void addBootstrapRegistration(JClass nodeClass, JDefinedClass innerInjectorClass) throws JClassAlreadyExistsException {
        if(injectorClass == null){
            injectorClass = generationUtil.defineClass(BOOTSTRAPS_INJECTOR);
            injectorClass._implements(codeModel.ref(Repository.class).narrow(Bootstraps.BootstrapInjector.class));

            // map to hold injector instances
            JClass mapType = codeModel.ref(Map.class).narrow(Class.class, Bootstraps.BootstrapInjector.class);
            JClass hashmapType = codeModel.ref(HashMap.class).narrow(Class.class, Bootstraps.BootstrapInjector.class);
            registerField = injectorClass.field(JMod.PRIVATE, mapType, "registration", JExpr._new(hashmapType));

            // initalize constructor
            JMethod constructor = injectorClass.constructor(JMod.PUBLIC);
            registerBlock = constructor.body();

            // returns map
            JMethod method = injectorClass.method(JMod.PUBLIC, mapType, Bootstraps.BOOTSTRAPS_INJECTOR_GET);
            method.body()._return(registerField);

        }

        registerBlock.invoke(registerField, "put").arg(nodeClass.dotclass()).arg(JExpr._new(innerInjectorClass));
    }
}
