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
package org.androidtransfuse.adapter.element;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * Element specific AST Type
 *
 * @author John Ericksen
 */
public class ASTElementType extends ASTElementBase implements ASTType {

    private final ASTAccessModifier modifier;
    private final TypeElement typeElement;
    private final PackageClass packageClass;
    private final ImmutableSet<ASTMethod> methods;
    private final ImmutableSet<ASTConstructor> constructors;
    private final ImmutableSet<ASTField> fields;
    private final ImmutableSet<ASTType> interfaces;
    private final ImmutableSet<ASTType> innerTypes;
    private final ASTType superClass;

    public ASTElementType(ASTAccessModifier modifier, PackageClass packageClass,
                          TypeElement typeElement,
                          ImmutableSet<ASTConstructor> constructors,
                          ImmutableSet<ASTMethod> methods,
                          ImmutableSet<ASTField> fields,
                          ASTType superClass,
                          ImmutableSet<ASTType> interfaces,
                          ImmutableSet<ASTAnnotation> annotations,
                          ImmutableSet<ASTType> innerTypes) {
        super(typeElement, annotations);
        this.modifier = modifier;
        this.packageClass = packageClass;
        this.typeElement = typeElement;
        this.constructors = constructors;
        this.methods = methods;
        this.fields = fields;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.innerTypes = innerTypes;
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
    public ImmutableSet<ASTType> getInnerTypes() {
        return innerTypes;
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return modifier;
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
    public boolean isInterface() {
        return typeElement.getKind().isInterface();
    }

    public boolean isEnum() {
        return typeElement.getKind().equals(ElementKind.ENUM);
    }

    @Override
    public boolean isInnerClass() {
        return typeElement.getNestingKind() != NestingKind.TOP_LEVEL && !isStatic();
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
    public ImmutableList<ASTType> getGenericParameters() {
        return ImmutableList.of();
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
