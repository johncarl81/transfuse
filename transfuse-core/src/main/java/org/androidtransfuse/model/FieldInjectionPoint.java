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
package org.androidtransfuse.model;

import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class FieldInjectionPoint {

    private final ASTType rootContainingType;
    private final ASTType containingType;
    private final ASTField field;
    private final InjectionNode injectionNode;

    public FieldInjectionPoint(ASTType rootContainingType, ASTType containingType, ASTField field, InjectionNode injectionNode) {
        this.rootContainingType = rootContainingType;
        this.field = field;
        this.injectionNode = injectionNode;
        this.containingType = containingType;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }

    public ASTType getRootContainingType() {
        return rootContainingType;
    }

    public ASTType getContainingType() {
        return containingType;
    }

    public ASTField getField() {
        return field;
    }
}
