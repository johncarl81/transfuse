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
import org.androidtransfuse.Injectors;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.util.InjectorRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;


/**
 * @author John Ericksen
 */
public class InjectorsGenerator {

    public static final PackageClass INJECTORS_NAME = new PackageClass(Injectors.INJECTORS_PACKAGE, Injectors.INJECTORS_NAME);
    private static final PackageClass REPOSITORY_NAME = new PackageClass(Injectors.INJECTORS_PACKAGE, Injectors.INJECTORS_REPOSITORY_NAME);
    public static final String GET_METHOD = "get";
    private static final String MAP_NAME = "injectors";

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public InjectorsGenerator(JCodeModel codeModel, ClassGenerationUtil generationUtil) {
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
    }

    public JDefinedClass generateInjectors(Map<Provider<ASTType>, JDefinedClass> processedAggregate) {
        try {
            JDefinedClass injectorRepositoryClass = generationUtil.defineClass(REPOSITORY_NAME);

            injectorRepositoryClass._implements(InjectorRepository.class);

            //map definition
            JClass mapType = codeModel.ref(Map.class).narrow(Class.class, Object.class);
            JClass hashMapType = codeModel.ref(HashMap.class).narrow(Class.class, Object.class);
            JVar registrationMap = injectorRepositoryClass.field(JMod.PRIVATE | JMod.FINAL, mapType, MAP_NAME, JExpr._new(hashMapType));

            //getter
            JMethod getMethod = injectorRepositoryClass.method(JMod.PUBLIC, Object.class, GET_METHOD);
            JTypeVar t = getMethod.generify("T");
            getMethod.type(t);
            JVar typeParam = getMethod.param(codeModel.ref(Class.class).narrow(t), "type");

            getMethod.body()._return(JExpr.cast(t, registrationMap.invoke("get").arg(typeParam)));

            // constructor registration block
            JBlock injectorRegistrationBlock = injectorRepositoryClass.constructor(JMod.PUBLIC).body();

            for (Map.Entry<Provider<ASTType>, JDefinedClass> astTypeJDefinedClassEntry : processedAggregate.entrySet()) {
                JClass interfaceClass = codeModel.ref(astTypeJDefinedClassEntry.getKey().get().getName());

                //register injector implementations
                injectorRegistrationBlock.add(registrationMap.invoke("put")
                        .arg(interfaceClass.dotclass())
                        .arg(JExpr._new(astTypeJDefinedClassEntry.getValue())));
            }
            return injectorRepositoryClass;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Already generated Injectors class", e);
        }
    }
}
