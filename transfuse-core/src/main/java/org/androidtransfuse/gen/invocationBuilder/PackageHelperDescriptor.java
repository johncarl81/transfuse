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
package org.androidtransfuse.gen.invocationBuilder;

import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class PackageHelperDescriptor {

    private final PackageClass name;
    private final Map<ConstructorInjectionPoint, String> constructorMapping = new HashMap<ConstructorInjectionPoint, String>();
    private final Map<FieldInjectionPoint, String> fieldSetMapping = new HashMap<FieldInjectionPoint, String>();
    private final Map<FieldGetter, String> fieldGetMapping = new HashMap<FieldGetter, String>();
    private final Map<MethodCall, String> methodCallMapping = new HashMap<MethodCall, String>();

    public PackageHelperDescriptor(PackageClass name) {
        this.name = name;
    }

    public PackageClass getName() {
        return name;
    }

    public Map<ConstructorInjectionPoint, String> getConstructorMapping() {
        return constructorMapping;
    }

    public Map<FieldInjectionPoint, String> getFieldSetMapping() {
        return fieldSetMapping;
    }

    public Map<FieldGetter, String> getFieldGetMapping() {
        return fieldGetMapping;
    }

    public Map<MethodCall, String> getMethodCallMapping() {
        return methodCallMapping;
    }
}
