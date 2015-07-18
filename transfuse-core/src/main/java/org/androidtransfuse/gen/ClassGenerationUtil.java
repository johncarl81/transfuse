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
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.validation.Validator;

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
    private final Validator validator;

    @Inject
    public ClassGenerationUtil(JCodeModel codeModel, ClassGenerationStrategy classGenerationStrategy, Validator validator) {
        this.codeModel = codeModel;
        this.classGenerationStrategy = classGenerationStrategy;
        this.validator = validator;
    }

    public JDefinedClass defineClass(PackageClass className) throws JClassAlreadyExistsException {

        JPackage jPackage = codeModel._package(className.getPackage());

        JDefinedClass definedClass = jPackage._class(className.getClassName());

        classGenerationStrategy.annotateGenerated(definedClass);
        definedClass.annotate(SuppressWarnings.class).param("value", "unchecked");

        return definedClass;
    }

    public JType type(Class clazz){
        return codeModel._ref(clazz);
    }

    public JType type(ASTType astType){

        try {
            return codeModel.parseType(astType.getName());
        } catch (ClassNotFoundException e) {
            validator.error("Unable to parse type " + astType.getName()).element(astType).build();
            throw new TransfuseAnalysisException("Unable to parse type " + astType.getName(), e);
        } catch (ExceptionInInitializerError e){
            // Tried to initalize an Android class
            return codeModel.directClass(astType.getName());
        }
    }

    public JClass ref(PackageClass packageClass){
        return ref(packageClass.getFullyQualifiedName());
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
