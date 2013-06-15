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

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Element specific AST Type
 *
 * @author John Ericksen
 */
public class ASTElementType extends ASTElementBase implements ASTType {

    private final TypeElement typeElement;
    private final PackageClass packageClass;
    private final ASTElementConverterFactory astElementConverterFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private ImmutableSet<ASTMethod> methods = null;
    private ImmutableSet<ASTConstructor> constructors = null;
    private ImmutableSet<ASTField> fields = null;
    private ImmutableSet<ASTType> interfaces = null;
    private ASTType superClass = null;

    public ASTElementType(TypeElement typeElement,
                          PackageClass packageClass,
                          ImmutableSet<ASTAnnotation> annotations,
                          ASTElementConverterFactory astElementConverterFactory, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        super(typeElement, annotations);
        this.typeElement = typeElement;
        this.packageClass = packageClass;
        this.astElementConverterFactory = astElementConverterFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    @Override
    public String getName() {
        return packageClass.getCanonicalName();
    }

    @Override
    public synchronized PackageClass getPackageClass() {
        return packageClass;
    }

    @Override
    public synchronized ImmutableSet<ASTMethod> getMethods() {
        if(methods == null){
            loadMethods();
        }
        return methods;
    }

    private void loadMethods() {
        ImmutableSet.Builder<ASTMethod> methodsBuilder = ImmutableSet.builder();
        methods = methodsBuilder.addAll(transformAST(typeElement.getEnclosedElements(), ASTMethod.class)).build();
    }

    @Override
    public synchronized ImmutableSet<ASTField> getFields() {
        if(fields == null){
            loadFields();
        }
        return fields;
    }

    private void loadFields() {
        ImmutableSet.Builder<ASTField> fieldsBuilder = ImmutableSet.builder();
        fields = fieldsBuilder.addAll(transformAST(typeElement.getEnclosedElements(), ASTField.class)).build();
    }

    @Override
    public synchronized ImmutableSet<ASTConstructor> getConstructors() {
        if(constructors == null){
            loadConstructors();
        }
        return constructors;
    }

    private void loadConstructors() {
        ImmutableSet.Builder<ASTConstructor> constructorsBuilder = ImmutableSet.builder();
        constructors = constructorsBuilder.addAll(transformAST(typeElement.getEnclosedElements(), ASTConstructor.class)).build();
    }

    @Override
    public boolean isConcreteClass() {
        return typeElement.getKind().isClass();
    }

    @Override
    public synchronized ASTType getSuperClass() {
        if(superClass == null){
            loadSuperclass();
        }
        return superClass;
    }

    private void loadSuperclass() {
        if (typeElement.getSuperclass() != null) {
            superClass = typeElement.getSuperclass().accept(astTypeBuilderVisitor, null);
        }
    }

    @Override
    public synchronized ImmutableSet<ASTType> getInterfaces() {
        if(interfaces == null){
            loadInterfaces();
        }
        return interfaces;
    }

    private void loadInterfaces() {
        interfaces = FluentIterable.from(typeElement.getInterfaces())
                .transform(astTypeBuilderVisitor)
                .toImmutableSet();
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
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }

    @Override
    public String toString() {
        return getName();
    }

    public Element getElement() {
        return typeElement;
    }

    private <T extends ASTBase> List<T> transformAST(List<? extends Element> enclosedElements, Class<T> astType) {
        return FluentIterable
                .from(enclosedElements)
                .transform(astElementConverterFactory.buildASTElementConverter(astType))
                .filter(Predicates.notNull())
                .toImmutableList();
    }
}
