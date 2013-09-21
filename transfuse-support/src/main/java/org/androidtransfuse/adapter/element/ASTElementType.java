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
package org.androidtransfuse.adapter.element;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * Element specific AST Type
 *
 * @author John Ericksen
 */
public class ASTElementType extends ASTElementBase implements ASTType {

    private final TypeElement typeElement;
    private final PackageClass packageClass;
    private final ImmutableSet<ASTMethod> methods;
    private final ImmutableSet<ASTConstructor> constructors;
    private final ImmutableSet<ASTField> fields;
    private final ImmutableSet<ASTType> interfaces;
    private final ASTType superClass;

    public ASTElementType(PackageClass packageClass,
                          TypeElement typeElement,
                          ImmutableSet<ASTConstructor> constructors,
                          ImmutableSet<ASTMethod> methods,
                          ImmutableSet<ASTField> fields,
                          ASTType superClass,
                          ImmutableSet<ASTType> interfaces,
                          ImmutableSet<ASTAnnotation> annotations) {
        super(typeElement, annotations);
        this.packageClass = packageClass;
        this.typeElement = typeElement;
        this.constructors = constructors;
        this.methods = methods;
        this.fields = fields;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    @Override
    public String getName() {
        return packageClass.getCanonicalName();
    }

    @Override
    public PackageClass getPackageClass() {
        return packageClass;
    }

    @Override
    public ImmutableSet<ASTMethod> getMethods() {
        return methods;
    }

    @Override
    public ImmutableSet<ASTField> getFields() {
        return fields;
    }

    @Override
    public ImmutableSet<ASTConstructor> getConstructors() {
        return constructors;
    }

    @Override
    public boolean isConcreteClass() {
        return typeElement.getKind().isClass();
    }

    @Override
    public ASTType getSuperClass() {
        return superClass;
    }

    @Override
    public ImmutableSet<ASTType> getInterfaces() {
        return interfaces;
    }

    @Override
    public boolean isArray() {
        return false;
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

        return new EqualsBuilder().append(getName(), that.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).hashCode();
    }

    @Override
    public ImmutableSet<ASTType> getGenericParameters() {
        return ImmutableSet.of();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, true, true);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, false, true);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, true, false);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }

    @Override
    public String toString() {
        return getName();
    }
}
