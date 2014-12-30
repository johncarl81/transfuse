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
package org.androidtransfuse.gen.invocationBuilder;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.MethodSignature;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

public class ConstructorCall {
    private final ASTType type;
    private final List<ASTType> paramTypes;
    private final MethodSignature methodSignature;

    public ConstructorCall(ASTType type, List<ASTType> paramTypes) {
        this.type = type;
        this.paramTypes = paramTypes;
        this.methodSignature = new MethodSignature(type.getName(), paramTypes);
    }

    public ASTType getType() {
        return type;
    }

    public List<ASTType> getParamTypes() {
        return paramTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstructorCall)) {
            return false;
        }
        ConstructorCall that = (ConstructorCall) o;
        return new EqualsBuilder().append(type, that.type).append(methodSignature, that.methodSignature).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(type).append(methodSignature).hashCode();
    }
}