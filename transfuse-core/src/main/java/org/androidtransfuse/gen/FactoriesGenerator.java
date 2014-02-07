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
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.Repository;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FactoriesGenerator extends AbstractRepositoryGenerator {

    private static final PackageClass REPOSITORY_NAME = new PackageClass(Factories.FACTORIES_PACKAGE, Factories.FACTORIES_REPOSITORY_NAME);
    private static final String GET_METHOD = "get";

    private final ClassGenerationUtil generationUtil;
    private final ClassNamer classNamer;
    private final UniqueVariableNamer variableNamer;

    @Inject
    public FactoriesGenerator(ClassGenerationUtil generationUtil, ClassNamer classNamer, UniqueVariableNamer variableNamer) {
        super(Repository.class, generationUtil, variableNamer, REPOSITORY_NAME, Factories.FactoryBuilder.class);
        this.generationUtil = generationUtil;
        this.classNamer = classNamer;
        this.variableNamer = variableNamer;
    }

    @Override
    protected JExpression generateInstance(JDefinedClass factoryRepositoryClass, JClass interfaceClass, JClass concreteType) throws JClassAlreadyExistsException {

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

        return JExpr._new(factoryClass);
    }
}
