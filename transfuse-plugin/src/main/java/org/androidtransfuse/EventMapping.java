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
package org.androidtransfuse;

import org.androidtransfuse.adapter.ASTType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class EventMapping {

    private String methodName;
    private List<ASTType> methodArguments = new ArrayList<ASTType>();
    private Class<? extends Annotation> annotation;

    public EventMapping(String methodName, List<ASTType> methodArguments, Class<? extends Annotation> annotation) {
        this.methodName = methodName;
        if(methodArguments != null) {
            this.methodArguments.addAll(methodArguments);
        }
        this.annotation = annotation;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<ASTType> getMethodArguments() {
        return methodArguments;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
