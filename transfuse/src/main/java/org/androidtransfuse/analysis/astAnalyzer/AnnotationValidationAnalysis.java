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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.adapter.*;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.validation.ValidationBuilder;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;
import javax.tools.Diagnostic;

/**
 * @author John Ericksen
 */
public class AnnotationValidationAnalysis implements ASTAnalysis {

    private final Validator validator;

    @Inject
    public AnnotationValidationAnalysis(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        for (ASTConstructor astConstructor : astType.getConstructors()) {
            validateExtraValue(astConstructor);
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {
        validateExtraValue(astMethod);

        for (ASTParameter astParameter : astMethod.getParameters()) {
            validateExtraValue(astParameter);
        }
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTType concreteType, ASTField astField, AnalysisContext context) {
        validateExtraValue(astField);
    }

    private void validateExtraValue(ASTBase astBase){
        if(astBase.isAnnotated(Extra.class)){
            ASTAnnotation extraAnnotation = astBase.getASTAnnotation(Extra.class);

            String extraId = extraAnnotation.getProperty("value", String.class);

            if(!extraId.matches("^[a-zA-Z][a-zA-Z0-9]*$")){
                validator.add(ValidationBuilder.validator(Diagnostic.Kind.ERROR, "@Extra value must follow Java Bean syntax")
                        .element(astBase)
                        .annotation(extraAnnotation)
                        .value("value")
                        .build());
            }
        }
    }
}
