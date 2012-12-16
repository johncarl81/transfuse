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

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.TransfuseAnnotationProcessor;
import org.androidtransfuse.util.Generated;

import java.text.DateFormat;
import java.util.Date;

/**
 * Generation class centralizing the addition of the Generated annotation:
 * {@code @Generated(value = "org.androidtransfuse.TransfuseAnnotationProcessor", date = "7/12/12 10:08 AM")}
 *
 * @author John Ericksen
 */
public final class GeneratedClassAnnotator {

    private GeneratedClassAnnotator() {
        //noop utility class constructor
    }

    /**
     * Annotates the input class with the {@code @Generated} annotation
     * defineClass
     *
     * @param definedClass input codemodel class
     */
    public static void annotateGeneratedClass(JDefinedClass definedClass) {
        definedClass.annotate(Generated.class)
                .param("value", TransfuseAnnotationProcessor.class.getName())
                .param("date", DateFormat.getInstance().format(new Date()));
    }
}
