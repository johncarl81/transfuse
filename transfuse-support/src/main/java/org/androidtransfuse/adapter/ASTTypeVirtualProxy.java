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
package org.androidtransfuse.adapter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.TransfuseAdapterException;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.lang.annotation.Annotation;

/**
 * @author John Ericksen
 */
public class ASTTypeVirtualProxy implements ASTType {

    private final PackageClass packageClass;
    private ASTType proxy;

    public ASTTypeVirtualProxy(PackageClass packageClass) {
        this.packageClass = packageClass;
    }

    public void load(ASTType proxy) {
        this.proxy = proxy;
    }

    public boolean isLoaded() {
        return proxy != null;
    }

    private ASTType getProxy() {
        if (proxy != null) {
            return proxy;
        }
        throw new TransfuseAdapterException("Proxy not initialized prior to use: " + packageClass.getCanonicalName());
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return getProxy().getAccessModifier();
    }

    @Override
    public ImmutableSet<ASTMethod> getMethods() {
        return getProxy().getMethods();
    }

    @Override
    public boolean isFinal() {
        return getProxy().isFinal();
    }

    @Override
    public ImmutableSet<ASTField> getFields() {
        return getProxy().getFields();
    }

    @Override
    public ImmutableSet<ASTConstructor> getConstructors() {
        return getProxy().getConstructors();
    }

    @Override
    public boolean isConcreteClass() {
        return getProxy().isConcreteClass();
    }

    @Override
    public boolean isInterface() {
        return getProxy().isInterface();
    }

    @Override
    public boolean isEnum() {
        return getProxy().isEnum();
    }

    @Override
    public boolean isAbstract() {
        return getProxy().isAbstract();
    }

    @Override
    public boolean isStatic() {
        return getProxy().isStatic();
    }

    @Override
    public boolean isInnerClass() {
        return getProxy().isInnerClass();
    }

    @Override
    public ASTType getSuperClass() {
        return getProxy().getSuperClass();
    }

    @Override
    public ImmutableSet<ASTType> getInnerTypes() {
        return getProxy().getInnerTypes();
    }

    @Override
    public ImmutableSet<ASTType> getInterfaces() {
        return getProxy().getInterfaces();
    }

    @Override
    public ImmutableList<ASTType> getGenericArgumentTypes() {
        return getProxy().getGenericArgumentTypes();
    }

    @Override
    public ImmutableList<ASTGenericArgument> getGenericArguments() {
        return getProxy().getGenericArguments();
    }

    @Override
    public boolean inherits(ASTType type) {
        return getProxy().inherits(type);
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return getProxy().isAnnotated(annotation);
    }

    @Override
    public boolean isAnnotated(ASTType annotation) {
        return getProxy().isAnnotated(annotation);
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return getProxy().getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return getProxy().getAnnotation(annotation);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return getProxy().getASTAnnotation(annotation);
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTType)) {
            return false;
        }

        ASTType that = (ASTType) o;

        return new EqualsBuilder().append(proxy, that).isEquals();
    }

    @Override
    public int hashCode() {
        return proxy != null ? proxy.hashCode() : 0;
    }

    public String toString() {
        return getProxy().toString();
    }
}
