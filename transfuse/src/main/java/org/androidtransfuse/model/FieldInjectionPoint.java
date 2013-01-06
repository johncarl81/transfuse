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

import org.androidtransfuse.adapter.ASTAccessModifier;
import org.androidtransfuse.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class FieldInjectionPoint {

    private final ASTType containingType;
    private final InjectionNode injectionNode;
    private final String name;
    private final ASTAccessModifier modifier;

    public FieldInjectionPoint(ASTType containingType, ASTAccessModifier modifier, String name, InjectionNode injectionNode) {
        this.modifier = modifier;
        this.injectionNode = injectionNode;
        this.name = name;
        this.containingType = containingType;
    }

    public String getName() {
        return name;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    public ASTType getContainingType() {
        return containingType;
    }
}
