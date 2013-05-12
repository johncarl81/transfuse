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
package org.androidtransfuse.analysis.astAnalyzer.validation;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author John Ericksen
 */
public class AnnotationValidatorBuilder {

    private final ASTClassFactory astClassFactory;
    private final Validator validator;
    private final Map<ASTType, Set<GivenAnnotationValidationBuilder>> givenMap = new HashMap<ASTType, Set<GivenAnnotationValidationBuilder>>();

    @Inject
    public AnnotationValidatorBuilder(ASTClassFactory astClassFactory, Validator validator) {
        this.astClassFactory = astClassFactory;
        this.validator = validator;
    }

    public AnnotationValidator build(){

        ImmutableMap<ASTType, Set<AnnotationValidator>> annotationValidators = ImmutableMap.copyOf(
            Maps.transformValues(givenMap, new Function<Set<GivenAnnotationValidationBuilder>, Set<AnnotationValidator>>() {
                @Override
                public Set<AnnotationValidator> apply(Set<GivenAnnotationValidationBuilder> input) {
                    ImmutableSet.Builder<AnnotationValidator> validationSetBuilder = ImmutableSet.builder();

                    for (GivenAnnotationValidationBuilder givenAnnotationValidationBuilder : input) {
                        validationSetBuilder.add(givenAnnotationValidationBuilder.build());
                    }

                    return validationSetBuilder.build();
                }
        }));


        return new MultiAnnotationValidator(annotationValidators);
    }


    public GivenAnnotationValidationBuilder given(Class<? extends Annotation> annotationClass) {
        ASTType type = astClassFactory.getType(annotationClass);

        if(!givenMap.containsKey(type)){
            givenMap.put(type, new HashSet<GivenAnnotationValidationBuilder>());
        }

        GivenAnnotationValidationBuilder builder = new GivenAnnotationValidationBuilder();
        givenMap.get(type).add(builder);

        return builder;
    }

    public class GivenAnnotationValidationBuilder{

        private AnnotationValidator annotationValidator;

        public AnnotationValidator build(){
            return annotationValidator;
        }

        public void requires(Class<? extends Annotation> annotationClass, String message) {
            requires(Arrays.< Class<? extends Annotation>>asList(annotationClass), message);
        }

        public void requires(List<Class<? extends Annotation>> annotationClasses, String message) {
            ImmutableSet.Builder<ASTType> annotationTypes = ImmutableSet.builder();
            for (Class<? extends Annotation> annotationClass : annotationClasses) {
                annotationTypes.add(astClassFactory.getType(annotationClass));
            }
            annotationValidator = new AnnotationAccompaniesValidator(validator, annotationTypes.build(), message);
        }

        public void parameterMatches(String parameterName, String regex, String message) {
            annotationValidator = new AnnotationParameterRegexValidator(regex, parameterName, validator, message);
        }
    }
}
