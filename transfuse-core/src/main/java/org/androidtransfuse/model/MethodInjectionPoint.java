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
package org.androidtransfuse.model;

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class MethodInjectionPoint extends MethodInjectionPointBase {

    private final ASTType rootContainingType;
    private final ASTMethod method;

    public MethodInjectionPoint(ASTType rootContainingType, ASTType containingType, ASTMethod method) {
        super(containingType);
        this.rootContainingType = rootContainingType;
        this.method = method;
    }

    public ASTMethod getMethod() {
        return method;
    }

    public ASTType getRootContainingType() {
        return rootContainingType;
    }
}
