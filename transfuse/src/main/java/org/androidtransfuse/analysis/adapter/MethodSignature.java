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
package org.androidtransfuse.analysis.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class MethodSignature {

    private final String methodSignature;

    public MethodSignature(ASTMethod method) {
        this.methodSignature = makeDescriptor(method);
    }

    public MethodSignature(ASTType returnType, String methodName, List<ASTType> paramTypes){
        this.methodSignature = makeDescriptor(returnType, methodName, paramTypes);
    }

    /**
     * Makes a descriptor for a given method.
     *
     * @param method
     * @return descriptor
     */
    private String makeDescriptor(ASTMethod method) {
        List<ASTType> paramTypes = new ArrayList<ASTType>();

        for (ASTParameter parameter : method.getParameters()) {
            paramTypes.add(parameter.getASTType());
        }

        return makeDescriptor(method.getReturnType(), method.getName(), paramTypes);
    }

    private String makeDescriptor(ASTType returnType, String methodName, List<ASTType> params){
        return methodName + ':' + makeDescriptor(params, returnType);
    }

    /**
     * Makes a descriptor for a given method.
     *
     * @param paramTypes  parameter types.
     * @param retType return type.
     * @return method descriptor
     */
    private String makeDescriptor(List<ASTType> paramTypes, ASTType retType) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (ASTType paramType : paramTypes) {
            makeTypeDescriptor(builder, paramType);
        }

        builder.append(')');
        makeTypeDescriptor(builder, retType);
        return builder.toString();
    }

    private void makeTypeDescriptor(StringBuilder builder, ASTType type) {
        if (type.isArray()) {
            builder.append('[');
        }
        makeTypeDesc(builder, type);
    }

    private void makeTypeDesc(StringBuilder builder, ASTType type) {
        if (type instanceof ASTPrimitiveType) {
            builder.append(type.getName());
        } else {
            builder.append('L').append(type.getName().replace('.', '/')).append(';');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodSignature)) {
            return false;
        }

        MethodSignature that = (MethodSignature) o;

        return new EqualsBuilder().append(methodSignature, that.methodSignature).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(methodSignature).hashCode();
    }
}
