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
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.transaction.AbstractCompletionTransactionWorker;
import org.androidtransfuse.util.Repository;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Common generation for Reposiory classes
 *
 * @author John Ericksen
 */
public abstract class AbstractRepositoryGenerator extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, JDefinedClass> {

    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;

    private final PackageClass repositoryName;
    private final Class<?> repositoryType;
    private final Class<?> contentType;

    public AbstractRepositoryGenerator(Class<?> repositoryType, ClassGenerationUtil generationUtil, UniqueVariableNamer namer, PackageClass repositoryName, Class<?> contentType){
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.repositoryName = repositoryName;
        this.repositoryType = repositoryType;
        this.contentType = contentType;
    }

    @Override
    public JDefinedClass innerRun(Map<Provider<ASTType>, JDefinedClass> aggregate) {
        return generate(aggregate);
    }

    public JDefinedClass generate(Map<Provider<ASTType>, JDefinedClass> aggregate) {
        try {
            JDefinedClass factoryRepositoryClass = generationUtil.defineClass(repositoryName);

            factoryRepositoryClass._implements(generationUtil.ref(repositoryType).narrow(contentType));

            //map definition
            JClass mapType = generationUtil.ref(Map.class).narrow(Class.class, contentType);
            JClass hashMapType = generationUtil.ref(HashMap.class).narrow(Class.class, contentType);
            JVar registrationMap = factoryRepositoryClass.field(JMod.PRIVATE | JMod.FINAL, mapType, namer.generateName(mapType), JExpr._new(hashMapType));

            //get factory map
            JMethod getAll = factoryRepositoryClass.method(JMod.PUBLIC, mapType, Repository.GET_METHOD);
            getAll.body()._return(registrationMap);

            generateRegistration(factoryRepositoryClass, aggregate, registrationMap);

            return factoryRepositoryClass;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Already generated Factories class", e);
        }
    }

    private void generateRegistration(JDefinedClass factoryRepositoryClass, Map<Provider<ASTType>, JDefinedClass> processedAggregate, JVar registrationMap) throws JClassAlreadyExistsException{
        // constructor registration block
        JBlock factoryRegistrationBlock = factoryRepositoryClass.constructor(JMod.PUBLIC).body();

        for (Map.Entry<Provider<ASTType>, JDefinedClass> astTypeJDefinedClassEntry : processedAggregate.entrySet()) {

            ASTType astType = astTypeJDefinedClassEntry.getKey().get();
            JClass interfaceClass = generationUtil.ref(astType);

            JDefinedClass mappedValue = astTypeJDefinedClassEntry.getValue();
            if(mappedValue != null){

                //register factory implementations
                factoryRegistrationBlock.add(registrationMap.invoke("put")
                        .arg(interfaceClass.dotclass())
                        .arg(generateInstance(factoryRepositoryClass, interfaceClass, mappedValue)));
            }
        }
    }

    protected abstract JExpression generateInstance(JDefinedClass factoryRepositoryClass, JClass interfaceClass, JClass concreteType) throws JClassAlreadyExistsException;
}
