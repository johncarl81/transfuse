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

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.util.Generated;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class unifying the creation of a basic class from a PackageClass
 *
 * Annotates the generated class with the recommended annotation:
 * {@code @Generated(value = "org.androidtransfuse.TransfuseAnnotationProcessor", date = "2001-07-04T12:08:56.235-0700")}
 *
 * @author John Ericksen
 */
public class ClassGenerationUtil {

    private static final Map<String, String> PROHIBITED_PACKAGES = new HashMap<String, String>();

    static{
        PROHIBITED_PACKAGES.put("java.", "java_.");
        PROHIBITED_PACKAGES. put("javax.", "javax_.");
    }

    // ISO 8601 standard date format
    private final DateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
    private final JCodeModel codeModel;
    private final Logger log;
    private final UniqueVariableNamer namer;

    @Inject
    public ClassGenerationUtil(JCodeModel codeModel, Logger log, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.log = log;
        this.namer = namer;
    }

    public String getPackage(String inputPackage){
        String validPackage = inputPackage;
        for (Map.Entry<String, String> prohibitedPackage : PROHIBITED_PACKAGES.entrySet()) {
            if(inputPackage.startsWith(prohibitedPackage.getKey())){
                validPackage = inputPackage.replaceFirst(prohibitedPackage.getKey(), prohibitedPackage.getValue());
            }
        }
        return validPackage;
    }

    public JDefinedClass defineClass(PackageClass className) throws JClassAlreadyExistsException {
        return defineClass(className, false);
    }

    public JDefinedClass defineClass(PackageClass className, boolean enforceUniqueName) throws JClassAlreadyExistsException {

        // Avoid prohibited package names
        String packageName = getPackage(className.getPackage());

        JPackage jPackage = codeModel._package(packageName);
        String name;
        if(enforceUniqueName){
            name = namer.generateClassName(className.getClassName());
        }
        else{
            name = className.getClassName();
        }
        JDefinedClass definedClass = jPackage._class(name);

        annotateGeneratedClass(definedClass);

        log.info("Generated " + className);

        return definedClass;
    }

    /**
     * Annotates the input class with the {@code @Generated} annotation
     * defineClass
     *
     * @param definedClass input codemodel class
     */
    private void annotateGeneratedClass(JDefinedClass definedClass) {
        definedClass.annotate(Generated.class)
                .param("value", "org.androidtransfuse.TransfuseAnnotationProcessor"/*TransfuseAnnotationProcessor.class.getName()*/)
                .param("date", iso8601.format(new Date()));
    }
}
