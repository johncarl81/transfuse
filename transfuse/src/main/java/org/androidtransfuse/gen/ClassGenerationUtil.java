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
import org.androidtransfuse.TransfuseAnnotationProcessor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.util.Generated;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class unifying the creation of a basic class from a PackageClass
 *
 * Annotates the generated class with the recommended annotation:
 * {@code @Generated(value = "org.androidtransfuse.TransfuseAnnotationProcessor", date = "2001-07-04T12:08:56.235-0700")}
 *
 * @author John Ericksen
 */
public class ClassGenerationUtil {

    // ISO 8601 standard date format
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    private final JCodeModel codeModel;
    private final Logger log;

    @Inject
    public ClassGenerationUtil(JCodeModel codeModel, Logger log) {
        this.codeModel = codeModel;
        this.log = log;
    }

    public JDefinedClass defineClass(PackageClass className) throws JClassAlreadyExistsException {
        JPackage jPackage = codeModel._package(className.getPackage());
        JDefinedClass definedClass = jPackage._class(className.getClassName());

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
                .param("value", TransfuseAnnotationProcessor.class.getName())
                .param("date", DATE_FORMAT.format(new Date()));
    }
}
