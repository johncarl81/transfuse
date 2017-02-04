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
package org.androidtransfuse.adapter.classes;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.util.TransfuseRuntimeException;

import javax.inject.Inject;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class LazyParametrizedTypeParameterBuilder implements LazyTypeParameterBuilder, Function<Type, ASTType> {

    private final ASTType originalType;
    private final ParameterizedType parameterizedType;
    private final ASTClassFactory astClassFactory;
    private ImmutableList<ASTType> genericParameters = null;

    @Inject
    public LazyParametrizedTypeParameterBuilder(/*@Assisted*/ ASTType originalType,
                                                /*@Assisted*/ ParameterizedType parameterizedType,
                                                ASTClassFactory astClassFactory) {
        this.originalType = originalType;
        this.parameterizedType = parameterizedType;
        this.astClassFactory = astClassFactory;
    }

    public synchronized ImmutableList<ASTType> buildGenericParameters() {
        if (genericParameters == null) {
            genericParameters = innerBuildGenericParameters();
        }
        return genericParameters;
    }

    private ImmutableList<ASTType> innerBuildGenericParameters() {
        return FluentIterable.from(Arrays.asList(parameterizedType.getActualTypeArguments()))
                .transform(this)
                .toList();
    }

    @Override
    public ASTType apply(Type input) {
        return getClass(input);
    }

    private ASTType getClass(Type type) {
        if (type instanceof Class) {
            return astClassFactory.getType((Class) type);
        } else if (type instanceof ParameterizedType) {
            ASTType rawType = getClass(((ParameterizedType) type).getRawType());
            return new ASTGenericTypeWrapper(rawType, new LazyParametrizedTypeParameterBuilder(originalType, (ParameterizedType)type, astClassFactory));
        } else if (type instanceof GenericArrayType) {
            return getClass(((GenericArrayType) type).getGenericComponentType());
        } else if(type instanceof TypeVariable){
            return new ASTGenericParameterType(new ASTGenericArgument(((TypeVariable)type).getName()), originalType);
        } else if(type instanceof WildcardType){
            WildcardType wildcardType = (WildcardType)type;
            ASTType extendsBound = null;
            ASTType superBound = null;
            if(wildcardType.getUpperBounds().length > 0){
                extendsBound = getClass(wildcardType.getUpperBounds()[0]);
            }
            if(wildcardType.getLowerBounds().length > 0){
                superBound = getClass(wildcardType.getLowerBounds()[0]);
            }
            return new ASTWildcardType(superBound, extendsBound);
        } else {
            throw new TransfuseRuntimeException("Unable to resolve Java Type to ASTType: " + type);
        }
    }
}
