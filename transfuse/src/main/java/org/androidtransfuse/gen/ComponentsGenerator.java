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
import org.androidtransfuse.Components;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.AbstractCompletionTransactionWorker;
import org.androidtransfuse.util.Repository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentsGenerator extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private static final PackageClass REPOSITORY_NAME = new PackageClass(Components.COMPONENTS_PACKAGE, Components.COMPONENTS_REPOSITORY_NAME);
    private static final String MAP_NAME = "components";
    private static final String GET_METHOD = "get";

    private final ClassGenerationUtil generationUtil;
    private final JCodeModel codeModel;

    @Inject
    public ComponentsGenerator(ClassGenerationUtil generationUtil, JCodeModel codeModel) {
        this.generationUtil = generationUtil;
        this.codeModel = codeModel;
    }

    @Override
    public Void innerRun(Map<Provider<ASTType>, JDefinedClass> components) {
        try {
            JDefinedClass injectorRepositoryClass = generationUtil.defineClass(REPOSITORY_NAME);

            injectorRepositoryClass._implements(codeModel.ref(Repository.class).narrow(Class.class));

            //map definition
            JClass mapType = codeModel.ref(Map.class).narrow(Class.class, Class.class);
            JClass hashMapType = codeModel.ref(HashMap.class).narrow(Class.class, Class.class);
            JVar registrationMap = injectorRepositoryClass.field(JMod.PRIVATE | JMod.FINAL, mapType, MAP_NAME, JExpr._new(hashMapType));

            //map getter
            JMethod getMapMethod = injectorRepositoryClass.method(JMod.PUBLIC, mapType, GET_METHOD);
            getMapMethod.body()._return(registrationMap);

            // constructor registration block
            JBlock injectorRegistrationBlock = injectorRepositoryClass.constructor(JMod.PUBLIC).body();

            for (Map.Entry<Provider<ASTType>, JDefinedClass> componentEntry : components.entrySet()) {
                JClass componentClass = codeModel.ref(componentEntry.getKey().get().getName());
                JClass androidClass = componentEntry.getValue();

                if(androidClass != null){
                    //register injector implementations
                    injectorRegistrationBlock.add(registrationMap.invoke("put")
                            .arg(componentClass.dotclass())
                            .arg(androidClass.dotclass()));
                }
            }
            return null;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Already generated Injectors class", e);
        }
    }
}
