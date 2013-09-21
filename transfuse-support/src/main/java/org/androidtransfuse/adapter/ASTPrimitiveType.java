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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.lang.annotation.Annotation;

/**
 * Element specific primitive AST type
 *
 * @author John Ericksen
 */
public enum ASTPrimitiveType implements ASTType {

    BOOLEAN("boolean", Boolean.class),
    BYTE("byte", Byte.class),
    SHORT("short", Short.class),
    CHAR("char", Character.class),
    INT("int", Integer.class),
    FLOAT("float", Float.class),
    LONG("long", Long.class),
    DOUBLE("double", Double.class);

    private static final ImmutableMap<String, ASTPrimitiveType> AUTOBOX_TYPE_MAP;

    private final Class clazz;
    private final String label;

    static {
        ImmutableMap.Builder<String, ASTPrimitiveType> autoboxTypeMapBuilder = ImmutableMap.builder();
        for (ASTPrimitiveType astPrimitive : ASTPrimitiveType.values()) {
            autoboxTypeMapBuilder.put(astPrimitive.getObjectClass().getName(), astPrimitive);
        }

        AUTOBOX_TYPE_MAP = autoboxTypeMapBuilder.build();
    }

    private ASTPrimitiveType(String label, Class clazz) {
        this.label = label;
        this.clazz = clazz;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return false;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return null;
    }

    @Override
    public ImmutableSet<ASTMethod> getMethods() {
        return ImmutableSet.of();
    }

    @Override
    public ImmutableSet<ASTField> getFields() {
        return ImmutableSet.of();
    }

    @Override
    public ImmutableSet<ASTConstructor> getConstructors() {
        return ImmutableSet.of();
    }

    @Override
    public String getName() {
        return label;
    }

    public Class getObjectClass() {
        return clazz;
    }

    @Override
    public boolean isConcreteClass() {
        return false;
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return ImmutableSet.of();
    }

    @Override
    public ImmutableSet<ASTType> getInterfaces() {
        return ImmutableSet.of();
    }

    @Override
    public ASTType getSuperClass() {
        return null;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public ImmutableSet<ASTType> getGenericParameters() {
        return ImmutableSet.of();
    }

    public static ASTPrimitiveType getAutoboxType(String name) {
        return AUTOBOX_TYPE_MAP.get(name);
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return type == this;
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return type == this;
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return type == this;
    }

    @Override
    public PackageClass getPackageClass() {
        return new PackageClass(null, label);
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
