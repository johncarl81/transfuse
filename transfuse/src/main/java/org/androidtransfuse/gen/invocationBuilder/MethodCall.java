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

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.MethodSignature;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

public class MethodCall {
    private final ASTType type;
    private final ASTType returnType;
    private final String methodName;
    private final List<ASTType> paramTypes;
    private final MethodSignature methodSignature;

    public MethodCall(ASTType type, ASTType returnType, String methodName, List<ASTType> paramTypes) {
        this.type = type;
        this.returnType = returnType;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.methodSignature = new MethodSignature(returnType, methodName, paramTypes);
    }

    public ASTType getType() {
        return type;
    }

    public ASTType getReturnType() {
        return returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<ASTType> getParamTypes() {
        return paramTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodCall)) {
            return false;
        }
        MethodCall that = (MethodCall) o;
        return new EqualsBuilder().append(type, that.type).append(methodSignature, that.methodSignature).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(type).append(methodSignature).hashCode();
    }
}