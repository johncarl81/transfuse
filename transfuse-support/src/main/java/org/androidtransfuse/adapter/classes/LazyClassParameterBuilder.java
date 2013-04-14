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
package org.androidtransfuse.adapter.classes;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.LazyTypeParameterBuilder;

import javax.inject.Inject;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class LazyClassParameterBuilder implements LazyTypeParameterBuilder, Function<Type, ASTType> {

    private final ParameterizedType parameterizedType;
    private final ASTClassFactory astClassFactory;
    private ImmutableSet<ASTType> genericParameters = null;

    @Inject
    public LazyClassParameterBuilder(/*@Assisted*/ ParameterizedType parameterizedType,
                                     ASTClassFactory astClassFactory) {
        this.parameterizedType = parameterizedType;
        this.astClassFactory = astClassFactory;
    }

    public ImmutableSet<ASTType> buildGenericParameters() {
        if (genericParameters == null) {
            genericParameters = innerBuildGenericParameters();
        }
        return genericParameters;
    }

    private ImmutableSet<ASTType> innerBuildGenericParameters() {
        return FluentIterable.from(Arrays.asList(parameterizedType.getActualTypeArguments()))
                .transform(this)
                .toSet();
    }

    @Override
    public ASTType apply(Type input) {
        Class clazz = getClass(input);

        if (clazz != null) {
            return astClassFactory.getType(clazz);
        }

        return null;
    }

    private Class<?> getClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else if(type instanceof TypeVariable){
            return getClass(((TypeVariable) type).getBounds()[0]);
        } else {
            return null;
        }
    }
}
