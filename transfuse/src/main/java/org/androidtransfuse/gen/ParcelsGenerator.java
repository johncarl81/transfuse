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

import android.os.Parcelable;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.util.ParcelableFactory;
import org.androidtransfuse.util.TransfuseRuntimeException;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ParcelsGenerator {

    public static final PackageClass PARCELS_NAME = new PackageClass("org.androidtransfuse", "Parcels");
    public static final String WRAP_METHOD = "wrap";

    private final ClassGenerationUtil classGenerationUtil;
    private final JCodeModel codeModel;
    private final UniqueVariableNamer uniqueVariableNamer;

    @Inject
    public ParcelsGenerator(ClassGenerationUtil classGenerationUtil, JCodeModel codeModel, UniqueVariableNamer uniqueVariableNamer) {
        this.classGenerationUtil = classGenerationUtil;
        this.codeModel = codeModel;
        this.uniqueVariableNamer = uniqueVariableNamer;
    }

    public void generate(Map<Provider<ASTType>, JDefinedClass> generated) {

        try {
            JDefinedClass parcelsDefinedClass = classGenerationUtil.defineClass(PARCELS_NAME);

            JClass mapRef = codeModel.ref(Map.class).narrow(Class.class, ParcelableFactory.class);
            JClass hashMapRef = codeModel.ref(HashMap.class).narrow(Class.class, ParcelableFactory.class);

            JFieldVar parcelWrappers = parcelsDefinedClass.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, mapRef, "parcelWrappers", JExpr._new(hashMapRef));

            JBlock staticInit = parcelsDefinedClass.init();

            for (Map.Entry<Provider<ASTType>, JDefinedClass> astTypeJDefinedClassEntry : generated.entrySet()) {

                JClass type = codeModel.ref(astTypeJDefinedClassEntry.getKey().get().getName());

                String innerClassName = uniqueVariableNamer.generateClassName(astTypeJDefinedClassEntry.getValue()) + "Factory";

                JDefinedClass factoryDefinedClass = parcelsDefinedClass._class(JMod.PRIVATE | JMod.STATIC, innerClassName);

                factoryDefinedClass._implements(codeModel.ref(ParcelableFactory.class).narrow(type));

                JMethod method = factoryDefinedClass.method(JMod.PUBLIC, astTypeJDefinedClassEntry.getValue(), ParcelableFactory.BUILD_PARCELABLE);
                JVar input = method.param(type, "input");

                method.body()._return(JExpr._new(astTypeJDefinedClassEntry.getValue()).arg(input));


                staticInit.invoke(parcelWrappers, "put").arg(type.staticRef("class")).arg(JExpr._new(factoryDefinedClass));
            }

            JMethod method = parcelsDefinedClass.method(JMod.PUBLIC | JMod.STATIC, Parcelable.class, WRAP_METHOD);

            JVar input = method.param(Object.class, "input");

            JInvocation wrapper = parcelWrappers.invoke("get").arg(input.invoke("getClass"));

            method.body()._return(wrapper.invoke(ParcelableFactory.BUILD_PARCELABLE).arg(input));

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseRuntimeException("Class already exists", e);
        }


    }
}
