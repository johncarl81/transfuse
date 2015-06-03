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
public abstract class AbstractRepositoryGenerator<T> extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, T>, JDefinedClass> {

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
    public JDefinedClass innerRun(Map<Provider<ASTType>, T> aggregate) {
        return generate(aggregate);
    }

    public JDefinedClass generate(Map<Provider<ASTType>, T> aggregate) {
        try {
            if(aggregate.isEmpty()){
                // don't generate a repository if it will hold nothing.
                return null;
            }

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

    private void generateRegistration(JDefinedClass factoryRepositoryClass, Map<Provider<ASTType>, T> processedAggregate, JVar registrationMap) throws JClassAlreadyExistsException{
        // constructor registration block
        JBlock factoryRegistrationBlock = factoryRepositoryClass.constructor(JMod.PUBLIC).body();

        for (Map.Entry<Provider<ASTType>, T> astTypeJDefinedClassEntry : processedAggregate.entrySet()) {

            ASTType astType = astTypeJDefinedClassEntry.getKey().get();
            JClass interfaceClass = generationUtil.ref(astType);

            T mappedValue = astTypeJDefinedClassEntry.getValue();
            if(mappedValue != null){

                Map<? extends JExpression, ? extends JExpression> mapping = generateMapping(factoryRepositoryClass, interfaceClass, mappedValue);

                for (Map.Entry<? extends JExpression, ? extends JExpression> mappingEntry : mapping.entrySet()) {
                    //register implementations
                    factoryRegistrationBlock.add(registrationMap.invoke("put")
                            .arg(mappingEntry.getKey())
                            .arg(mappingEntry.getValue()));
                }


            }
        }
    }

    protected abstract Map<? extends JExpression, ? extends JExpression> generateMapping(JDefinedClass factoryRepositoryClass, JClass interfaceClass, T concreteType) throws JClassAlreadyExistsException;
}
