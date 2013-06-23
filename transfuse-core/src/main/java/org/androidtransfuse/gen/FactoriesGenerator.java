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
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.Repository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;


/**
 * @author John Ericksen
 */
public class FactoriesGenerator {

    public static final PackageClass FACTORIES_NAME = new PackageClass(Factories.FACTORIES_PACKAGE, Factories.FACTORIES_NAME);
    private static final PackageClass REPOSITORY_NAME = new PackageClass(Factories.FACTORIES_PACKAGE, Factories.FACTORIES_REPOSITORY_NAME);
    public static final String GET_METHOD = "get";
    private static final String MAP_NAME = "factories";

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;

    @Inject
    public FactoriesGenerator(JCodeModel codeModel, ClassGenerationUtil generationUtil, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
        this.namer = namer;
    }

    public JDefinedClass generateFactories(Map<Provider<ASTType>, JDefinedClass> processedAggregate) {
        try {
            JDefinedClass factoryRepositoryClass = generationUtil.defineClass(REPOSITORY_NAME);

            factoryRepositoryClass._implements(codeModel.ref(Repository.class).narrow(Factories.FactoryBuilder.class));

            //map definition
            JClass mapIntances = codeModel.ref(Map.class).narrow(Class.class, Factories.FactoryBuilder.class);
            JClass hashMapIntances = codeModel.ref(HashMap.class).narrow(Class.class, Factories.FactoryBuilder.class);
            JVar registrationMap = factoryRepositoryClass.field(JMod.PRIVATE | JMod.FINAL, mapIntances, MAP_NAME, JExpr._new(hashMapIntances));

            //get factory map
            JMethod getAll = factoryRepositoryClass.method(JMod.PUBLIC, mapIntances, "get");
            getAll.body()._return(registrationMap);

            generateRegistration(factoryRepositoryClass, processedAggregate, registrationMap);

            return factoryRepositoryClass;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Already generated Factories class", e);
        }
    }

    private void generateRegistration(JDefinedClass factoryRepositoryClass, Map<Provider<ASTType>, JDefinedClass> processedAggregate, JVar registrationMap) throws JClassAlreadyExistsException {
        // constructor registration block
        JBlock factoryRegistrationBlock = factoryRepositoryClass.constructor(JMod.PUBLIC).body();

        for (Map.Entry<Provider<ASTType>, JDefinedClass> astTypeJDefinedClassEntry : processedAggregate.entrySet()) {
            ASTType astType = astTypeJDefinedClassEntry.getKey().get();
            JClass interfaceClass = generationUtil.ref(astType);

            //factory builder
            JDefinedClass factoryClass = factoryRepositoryClass._class(JMod.PRIVATE | JMod.FINAL | JMod.STATIC, namer.generateClassName(astType));
            factoryClass._implements(generationUtil.ref(Factories.FactoryBuilder.class).narrow(interfaceClass));

            //getter without given scopes
            JMethod getMethod = factoryClass.method(JMod.PUBLIC, interfaceClass, GET_METHOD);

            getMethod.body()._return(JExpr._new(astTypeJDefinedClassEntry.getValue()));

            //getter with scopes
            JMethod getMethodWithScopes = factoryClass.method(JMod.PUBLIC, interfaceClass, GET_METHOD);
            JVar scopes = getMethodWithScopes.param(codeModel.ref(Scopes.class), namer.generateName(Scopes.class));

            getMethodWithScopes.body()._return(JExpr._new(astTypeJDefinedClassEntry.getValue()).arg(scopes));

            //register factory implementations
            factoryRegistrationBlock.add(registrationMap.invoke("put")
                    .arg(interfaceClass.dotclass())
                    .arg(JExpr._new(factoryClass)));
        }
    }
}
