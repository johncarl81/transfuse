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
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.annotation.Annotation;

/**
 * Specific AST Array Type.
 * <p/>
 * This ASTType wraps a delegate ASTType, decorating it with array attributes.
 *
 * @author John Ericksen
 */
public class ASTArrayType implements ASTType {

    private final ASTType delegate;

    public ASTArrayType(ASTType delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return delegate.isAnnotated(annotation);
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return delegate.getAnnotation(annotation);
    }

    @Override
    public String getName() {
        return delegate.getName() + "[]";
    }

    @Override
    public ImmutableSet<ASTMethod> getMethods() {
        return delegate.getMethods();
    }

    @Override
    public ImmutableSet<ASTField> getFields() {
        return delegate.getFields();
    }

    @Override
    public ImmutableSet<ASTConstructor> getConstructors() {
        return delegate.getConstructors();
    }

    @Override
    public boolean isConcreteClass() {
        return delegate.isConcreteClass();
    }

    @Override
    public ASTType getSuperClass() {
        return delegate.getSuperClass();
    }

    @Override
    public ImmutableSet<ASTType> getInterfaces() {
        return delegate.getInterfaces();
    }

    @Override
    public ImmutableSet<ASTType> getGenericParameters() {
        return ImmutableSet.of();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return delegate.inheritsFrom(type);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return delegate.extendsFrom(type);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return delegate.implementsFrom(type);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return delegate.getASTAnnotation(annotation);
    }

    @Override
    public PackageClass getPackageClass() {
        return delegate.getPackageClass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTArrayType)) {
            return false;
        }

        ASTArrayType that = (ASTArrayType) o;

        return new EqualsBuilder().append(isArray(), that.isArray()).append(delegate, that.delegate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(delegate).hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
