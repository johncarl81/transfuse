/**
 * Copyright 2012 John Ericksen
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

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.model.PackageClass;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTGenericTypeWrapper implements ASTType {

    private final ASTType astType;
    private final LazyTypeParameterBuilder lazyTypeParameterBuilder;

    @Inject
    public ASTGenericTypeWrapper(@Assisted ASTType astType, @Assisted LazyTypeParameterBuilder lazyTypeParameterBuilder) {
        this.astType = astType;
        this.lazyTypeParameterBuilder = lazyTypeParameterBuilder;
    }

    @Override
    public List<ASTType> getGenericParameters() {
        return lazyTypeParameterBuilder.buildGenericParameters();
    }

    public Collection<ASTMethod> getMethods() {
        return astType.getMethods();
    }

    public Collection<ASTField> getFields() {
        return astType.getFields();
    }

    public Collection<ASTConstructor> getConstructors() {
        return astType.getConstructors();
    }

    public boolean isConcreteClass() {
        return astType.isConcreteClass();
    }

    public ASTType getSuperClass() {
        return astType.getSuperClass();
    }

    public Collection<ASTType> getInterfaces() {
        return astType.getInterfaces();
    }

    public boolean isArray() {
        return astType.isArray();
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return astType.isAnnotated(annotation);
    }

    public Collection<ASTAnnotation> getAnnotations() {
        return astType.getAnnotations();
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return astType.getAnnotation(annotation);
    }

    public String getName() {
        return astType.getName();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return astType.inheritsFrom(type);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return astType.extendsFrom(type);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return astType.implementsFrom(type);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return astType.getASTAnnotation(annotation);
    }

    @Override
    public PackageClass getPackageClass() {
        return astType.getPackageClass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTType)) {
            return false;
        }

        ASTType that = (ASTType) o;

        return new EqualsBuilder().append(getName(), that.getName()).isEquals() &&
                ListUtils.isEqualList(this.getGenericParameters(), that.getGenericParameters());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append('<');
        builder.append(StringUtils.join(getGenericParameters(), ","));
        builder.append('>');

        return getName() + builder.toString();
    }
}
