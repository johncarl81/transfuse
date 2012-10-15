package org.androidtransfuse.analysis.adapter;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @author John Ericksen
 */
public class LazyClassParameterBuilder implements LazyTypeParameterBuilder, Function<Type, ASTType> {

    private final ParameterizedType parameterizedType;
    private final ASTClassFactory astClassFactory;
    private ImmutableList<ASTType> genericParameters = null;

    @Inject
    public LazyClassParameterBuilder(@Assisted ParameterizedType parameterizedType,
                                     ASTClassFactory astClassFactory) {
        this.parameterizedType = parameterizedType;
        this.astClassFactory = astClassFactory;
    }

    public List<ASTType> buildGenericParameters() {
        if (genericParameters == null) {
            genericParameters = innerBuildGenericParameters();
        }
        return genericParameters;
    }

    private ImmutableList<ASTType> innerBuildGenericParameters() {
        return FluentIterable.from(Arrays.asList(parameterizedType.getActualTypeArguments()))
                .transform(this)
                .toImmutableList();
    }

    @Override
    public ASTType apply(Type input) {
        Class clazz = getClass(input);

        if (clazz != null) {
            return astClassFactory.buildASTClassType(clazz);
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
        } else {
            return null;
        }
    }
}
