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
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.ScopeAspectFactoryRepository;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.scope.Scopes;

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
    private final ScopeAspectFactoryRepository scopeRepository;

    private JDefinedClass injectorClass = null;
    private JBlock registerBlock = null;
    private JFieldVar registerField = null;

    public BootstrapsInjectorGenerator(JCodeModel codeModel,
                                       ClassGenerationUtil generationUtil,
                                       UniqueVariableNamer namer,
                                       InjectionFragmentGenerator injectionGenerator,
                                       ExistingVariableInjectionBuilderFactory variableBuilderFactory,
                                       ScopeAspectFactoryRepository scopeRepository) {
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.injectionGenerator = injectionGenerator;
        this.variableBuilderFactory = variableBuilderFactory;
        this.scopeRepository = scopeRepository;
    }

    public void generate(InjectionNode injectionNode){

        try {

            getInjectorClass();

            JClass nodeClass = codeModel.ref(injectionNode.getClassName());

            // add injector class
            JDefinedClass innerInjectorClass = generationUtil.defineClass(injectionNode.getASTType().getPackageClass().append(Bootstraps.IMPL_EXT));

            innerInjectorClass._extends(codeModel.ref(Bootstraps.BootstrapInjector.class).narrow(nodeClass));

            JMethod method = innerInjectorClass.method(JMod.PUBLIC, codeModel.VOID, Bootstraps.BOOTSTRAPS_INJECTOR_METHOD);
            method.param(codeModel.ref(Class.class).narrow(nodeClass), "key");
            JVar input = method.param(nodeClass, "input");
            JBlock injectorBlock = method.body();

            //define root scope holder
            JClass scopesRef = codeModel.ref(Scopes.class);
            JVar scopes = injectorBlock.decl(scopesRef, namer.generateName(Scopes.class), JExpr._new(scopesRef));

            //define scopes
            for (Map.Entry<ASTType, ASTType> scopeEntry : scopeRepository.getScopeAnnotations().entrySet()) {
                JExpression annotation = codeModel.ref(scopeEntry.getKey().getName()).dotclass();
                JClass scopeType = codeModel.ref(scopeEntry.getValue().getName());

                injectorBlock.invoke(scopes, Scopes.ADD_SCOPE).arg(annotation).arg(JExpr._new(scopeType));
            }


            injectorBlock.add(JExpr.invoke("scopeSingletons").arg(scopes));

            setupInjectionAspect(injectionNode, input);

            injectionGenerator.buildFragment(injectorBlock, innerInjectorClass, injectionNode, scopes);

            // add instance to map
            registerBlock.invoke(registerField, "put").arg(nodeClass.dotclass()).arg(JExpr._new(innerInjectorClass));

        } catch (JClassAlreadyExistsException e) {
            throw new BootstrapException("Unable to crate Bootstrap Injector, class already exists.", e);
        } catch (ClassNotFoundException e) {
            throw new BootstrapException("Unable to find class", e);
        }
    }

    private void setupInjectionAspect(InjectionNode injectionNode, JVar input) {
        injectionNode.addAspect(VariableBuilder.class, variableBuilderFactory.buildVariableBuilder(input));
    }

    private synchronized JDefinedClass getInjectorClass() throws JClassAlreadyExistsException {
        if(injectorClass == null){
            injectorClass = generationUtil.defineClass(BOOTSTRAPS_INJECTOR);
            injectorClass._implements(Bootstraps.BootstrapRepository.class);

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
        return injectorClass;
    }
}
