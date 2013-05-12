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
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.validation.Validator;

/**
 * @author John Ericksen
 */
public class AnnotationAccompaniesValidator implements AnnotationValidator {

    private final Validator validator;
    private final ImmutableSet<ASTType> requiredAnnotations;
    private final String message;

    public AnnotationAccompaniesValidator(Validator validator, ImmutableSet<ASTType> requiredAnnotations, String message) {
        this.validator = validator;
        this.requiredAnnotations = requiredAnnotations;
        this.message = message;
    }

    @Override
    public void validate(ASTAnnotation annotation, ASTBase astBase, ImmutableSet<ASTAnnotation> applicableAnnotations) {
        boolean found = false;
        for (ASTAnnotation applicableAnnotation : applicableAnnotations) {
            if(requiredAnnotations.contains(applicableAnnotation.getASTType())){
                found = true;
            }
        }
        if(!found){
            validator.error(message)
                    .element(astBase)
                    .annotation(annotation)
                    .build();
        }
    }
}
