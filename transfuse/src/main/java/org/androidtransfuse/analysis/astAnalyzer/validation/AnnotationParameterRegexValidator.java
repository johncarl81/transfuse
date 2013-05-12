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

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.validation.ValidationBuilder;
import org.androidtransfuse.validation.Validator;

import javax.tools.Diagnostic;

/**
 * @author John Ericksen
 */
public class AnnotationParameterRegexValidator implements AnnotationValidator{

    private final String regex;
    private final String parameter;
    private final Validator validator;
    private final String message;

    public AnnotationParameterRegexValidator(String regex, String parameter, Validator validator, String message) {
        this.regex = regex;
        this.parameter = parameter;
        this.validator = validator;
        this.message = message;
    }

    @Override
    public void validate(ASTAnnotation annotation, ASTBase astBase, ImmutableSet<ASTAnnotation> applicableAnnotations) {
        String parameterValue = annotation.getProperty(parameter, String.class);

        if(parameterValue != null && !parameterValue.matches(regex)){
            validator.add(ValidationBuilder.validator(Diagnostic.Kind.ERROR, message)
                    .element(astBase)
                    .annotation(annotation)
                    .value(parameter)
                    .build());
        }
    }
}
