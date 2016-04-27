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
package org.androidtransfuse.gen;

import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.*;
import org.androidtransfuse.Factories;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.Repository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class FactoriesGenerator extends AbstractRepositoryGenerator<JDefinedClass> {

    private static final PackageClass REPOSITORY_NAME = new PackageClass(Factories.FACTORIES_PACKAGE, Factories.FACTORIES_REPOSITORY_NAME);
    private static final String GET_METHOD = "get";

    private final ClassGenerationUtil generationUtil;
    private final ClassNamer classNamer;
    private final UniqueVariableNamer variableNamer;
    private final Boolean libraryProject;

    @Inject
    public FactoriesGenerator(ClassGenerationUtil generationUtil, ClassNamer classNamer, UniqueVariableNamer variableNamer, @Named("libraryProject") Boolean libraryProject) {
        super(Repository.class, generationUtil, variableNamer, REPOSITORY_NAME, Factories.FactoryBuilder.class);
        this.generationUtil = generationUtil;
        this.classNamer = classNamer;
        this.variableNamer = variableNamer;
        this.libraryProject = libraryProject;
    }

    @Override
    public JDefinedClass generate(Map<Provider<ASTType>, JDefinedClass> aggregate) {
        if(libraryProject) {
            return null;
        }
        return super.generate(aggregate);
    }

    @Override
    protected Map<? extends JExpression, ? extends JExpression> generateMapping(JDefinedClass factoryRepositoryClass, JClass interfaceClass, JDefinedClass concreteType) throws JClassAlreadyExistsException {

        //factory builder
        JDefinedClass factoryClass = factoryRepositoryClass._class(JMod.PRIVATE | JMod.FINAL | JMod.STATIC, classNamer.numberedClassName(interfaceClass).build().getClassName());
        factoryClass._implements(generationUtil.ref(Factories.FactoryBuilder.class).narrow(interfaceClass));

        //getter without given scopes
        JMethod getMethod = factoryClass.method(JMod.PUBLIC, interfaceClass, Repository.GET_METHOD);

        getMethod.body()._return(JExpr._new(concreteType));

        //getter with scopes
        JMethod getMethodWithScopes = factoryClass.method(JMod.PUBLIC, interfaceClass, GET_METHOD);
        JVar scopes = getMethodWithScopes.param(generationUtil.ref(Scopes.class), variableNamer.generateName(Scopes.class));

        getMethodWithScopes.body()._return(JExpr._new(concreteType).arg(scopes));

        return ImmutableMap.of(interfaceClass.dotclass(), JExpr._new(factoryClass));
    }
}
