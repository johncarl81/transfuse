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
package org.androidtransfuse.adapter;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Class to decorate an ASTMethod with an equals and hashCode based on the method signature.  This helps enforce
 * uniqueness of a method on a class throughout the inheritance hierarchy.
 *
 */
public class ASTMethodUniqueSignatureDecorator implements ASTMethod{
    private final ASTMethod method;
    private final MethodSignature methodSignature;

    public ASTMethodUniqueSignatureDecorator(ASTMethod method) {
        this.method = method;
        this.methodSignature = new MethodSignature(method);
    }

    @Override
    public List<ASTParameter> getParameters() {
        return method.getParameters();
    }

    @Override
    public ASTType getReturnType() {
        return method.getReturnType();
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return method.getAccessModifier();
    }

    @Override
    public ImmutableSet<ASTType> getThrowsTypes() {
        return method.getThrowsTypes();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return method.isAnnotated(annotation);
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return method.getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return method.getAnnotation(annotation);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return method.getASTAnnotation(annotation);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTMethodUniqueSignatureDecorator)) {
            return false;
        }

        ASTMethodUniqueSignatureDecorator that = (ASTMethodUniqueSignatureDecorator) o;

        return new EqualsBuilder().append(methodSignature, that.methodSignature).isEquals();
    }

    @Override
    public int hashCode() {
        return methodSignature.hashCode();
    }
}