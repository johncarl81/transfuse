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

import org.androidtransfuse.adapter.ASTType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FieldReference {
    private final ASTType returnType;
    private final ASTType variableType;
    private final String name;

    public FieldReference(ASTType returnType, ASTType variableType, String name) {
        this.returnType = returnType;
        this.variableType = variableType;
        this.name = name;
    }

    public ASTType getReturnType() {
        return returnType;
    }

    public ASTType getVariableType() {
        return variableType;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldReference)) {
            return false;
        }
        FieldReference that = (FieldReference) o;
        return new EqualsBuilder().append(name, that.name).append(variableType, that.variableType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(variableType).hashCode();
    }
}