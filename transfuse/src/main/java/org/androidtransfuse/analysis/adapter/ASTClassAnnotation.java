/**
 * Copyright 2012 John Ericksen
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

import com.google.common.base.Function;
import org.androidtransfuse.analysis.TransfuseAnalysisException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import static com.google.common.collect.Collections2.transform;


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

    public Collection<String> getPropertyNames(){
        return transform(Arrays.asList(annotation.annotationType().getDeclaredMethods()),
                new MethodNameExtractor());
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
}
