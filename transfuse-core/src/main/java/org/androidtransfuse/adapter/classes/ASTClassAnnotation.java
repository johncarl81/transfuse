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
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Class specific AST Annotation
 *
 * @author John Ericksen
 */
public class ASTClassAnnotation implements ASTAnnotation {

    private final Annotation annotation;
    private final ASTType type;
    private final ASTClassFactory astClassFactory;

    public ASTClassAnnotation(Annotation annotation, ASTType type, ASTClassFactory astClassFactory) {
        this.annotation = annotation;
        this.astClassFactory = astClassFactory;
        this.type = type;
    }

    public ImmutableSet<String> getPropertyNames(){
        return FluentIterable.from(Arrays.asList(annotation.annotationType().getDeclaredMethods()))
                .transform(new MethodNameExtractor())
                .toImmutableSet();
    }

    private static final class MethodNameExtractor implements Function<Method, String> {
        @Override
        public String apply(Method input) {
            return input.getName();
        }
    }

    @Override
    public <T> T getProperty(String name, Class<T> type) {
        try {
            Method annotationParameter = annotation.annotationType().getDeclaredMethod(name);

            Class convertedType = type;
            boolean convertToASTType = false;

            if (type.equals(ASTType.class)) {
                //convert classes into ASTType
                convertedType = Class.class;
                convertToASTType = true;
            }

            if (!convertedType.isAssignableFrom(annotationParameter.getReturnType())) {
                throw new TransfuseAnalysisException("Type not expected: " + convertedType);
            }

            Object invocationResult = annotationParameter.invoke(annotation);

            if (convertToASTType) {
                return (T) astClassFactory.getType((Class) invocationResult);
            }
            return (T) invocationResult;

        } catch (IllegalAccessException e) {
            throw new TransfuseAnalysisException("IllegalAccessException Exception while accessing annotation method: " + name, e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("Annotation method not present: " + name, e);
        } catch (InvocationTargetException e) {
            throw new TransfuseAnalysisException("InvocationTargetException Exception while accessing annotation method: " + name, e);
        }
    }

    @Override
    public ASTType getASTType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof ASTAnnotation)){
            return false;
        }

        ASTAnnotation that = (ASTAnnotation) o;

        if(!type.equals(that.getASTType())){
            return false;
        }

        Map<String, Object> thisProperties = new HashMap<String, Object>();
        Map<String, Object> thatProperties = new HashMap<String, Object>();

        for (String property : getPropertyNames()) {
            thisProperties.put(property, this.getProperty(property, Object.class));
            thatProperties.put(property, that.getProperty(property, Object.class));
        }

        return thisProperties.equals(thatProperties);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(type).hashCode();
    }
}
