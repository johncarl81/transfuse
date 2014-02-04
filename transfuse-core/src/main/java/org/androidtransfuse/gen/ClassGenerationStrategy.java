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

import com.sun.codemodel.JDefinedClass;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author John Ericksen
 */
public class ClassGenerationStrategy {

    // ISO 8601 standard date format
    private static final DateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    private final Class<? extends Annotation> generatedClass;
    private final String generatorName;

    public ClassGenerationStrategy(Class<? extends Annotation> generatedClass, String generatorName) {
        this.generatorName = generatorName;
        this.generatedClass = generatedClass;
    }

    public void annotateGenerated(JDefinedClass definedClass) {
        definedClass.annotate(generatedClass)
                .param("value", generatorName)
                .param("date", ISO_8601.format(new Date()));
    }
}
