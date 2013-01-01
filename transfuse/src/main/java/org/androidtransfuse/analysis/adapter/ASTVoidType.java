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

import org.androidtransfuse.model.PackageClass;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Globally represents the VOID type
 *
 * @author John Ericksen
 */
public enum ASTVoidType implements ASTType {
    VOID("void");

    private final String label;

    private ASTVoidType(String label) {
        this.label = label;
    }

    @Override
    public Collection<ASTMethod> getMethods() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ASTField> getFields() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ASTConstructor> getConstructors() {
        return Collections.emptySet();
    }

    @Override
    public boolean isConcreteClass() {
        return true;
    }

    @Override
    public ASTType getSuperClass() {
        return null;
    }

    @Override
    public Collection<ASTType> getInterfaces() {
        return Collections.emptySet();
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public List<ASTType> getGenericParameters() {
        return Collections.emptyList();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return false;
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return false;
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return false;
    }

    @Override
    public PackageClass getPackageClass() {
        return new PackageClass(null, label);
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return false;
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return Collections.emptySet();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return null;
    }

    @Override
    public String getName() {
        return label;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }

    @Override
    public String toString() {
        return getName();
    }
}
