/**
 * Copyright 2012 John Ericksen
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
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.AbstractCompletionTransactionWorker;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentsGenerator extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private static final PackageClass COMPONENTS_NAME = new PackageClass("org.androidtransfuse", "Components");
    private static final String MAP_NAME = "COMPONENTS";
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
            JDefinedClass injectorRepositoryClass = generationUtil.defineClass(COMPONENTS_NAME);

            //map definition
            JClass mapType = codeModel.ref(Map.class).narrow(Class.class, Class.class);
            JClass hashMapType = codeModel.ref(HashMap.class).narrow(Class.class, Class.class);
            JVar registrationMap = injectorRepositoryClass.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, mapType, MAP_NAME, JExpr._new(hashMapType));

            //getter
            JMethod getMethod = injectorRepositoryClass.method(JMod.PUBLIC | JMod.STATIC, Object.class, GET_METHOD);
            JTypeVar t = getMethod.generify("T");
            getMethod.type(codeModel.ref(Class.class).narrow(t));
            JVar typeParam = getMethod.param(codeModel.ref(Class.class).narrow(t), "type");

            getMethod.body()._return(JExpr.cast(codeModel.ref(Class.class).narrow(t), registrationMap.invoke("get").arg(typeParam)));

            //static registration block
            JBlock injectorRegistrationBlock = injectorRepositoryClass.init();

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
