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
import org.androidtransfuse.adapter.ASTArrayType;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class unifying the creation of a basic class from a PackageClass
 *
 * Annotates the generated class with the recommended annotation:
 * `@Generated(value = "org.androidtransfuse.TransfuseAnnotationProcessor", date = "2001-07-04T12:08:56.235-0700")`
 *
 * @author John Ericksen
 */
public class ClassGenerationUtil {

    private final JCodeModel codeModel;
    private final ClassGenerationStrategy classGenerationStrategy;

    @Inject
    public ClassGenerationUtil(JCodeModel codeModel, ClassGenerationStrategy classGenerationStrategy) {
        this.codeModel = codeModel;
        this.classGenerationStrategy = classGenerationStrategy;
    }

    public JDefinedClass defineClass(PackageClass className) throws JClassAlreadyExistsException {

        JPackage jPackage = codeModel._package(className.getPackage());

        JDefinedClass definedClass = jPackage._class(className.getClassName());

        classGenerationStrategy.annotateGenerated(definedClass);

        return definedClass;
    }

    public JClass ref(PackageClass packageClass){
        return ref(packageClass.getCanonicalName());
    }

    public JClass ref(ASTType astType){
        String typeName = astType.getName();
        if(astType instanceof ASTArrayType){
            return ref(typeName.substring(0, typeName.length() - 2)).array();
        }
        return ref(typeName);
    }

    public JClass narrowRef(ASTType astType){
        JClass reference = ref(astType);

        if(astType.getGenericParameters().size() > 0){
            List<JClass> genericParameterRefs = new ArrayList<JClass>();

            for (ASTType genericParam : astType.getGenericParameters()) {
                genericParameterRefs.add(narrowRef(genericParam));
            }

            reference = reference.narrow(genericParameterRefs);
        }

        return reference;
    }

    public JClass ref(String typeName){
        return codeModel.directClass(typeName);
    }

    public JClass ref(Class<?> clazz) {
        return codeModel.ref(clazz);
    }
}
