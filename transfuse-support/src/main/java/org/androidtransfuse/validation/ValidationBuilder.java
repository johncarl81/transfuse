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
package org.androidtransfuse.validation;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.adapter.element.ASTElementAnnotation;
import org.androidtransfuse.adapter.element.ASTElementField;
import org.androidtransfuse.adapter.element.ASTElementMethod;
import org.androidtransfuse.adapter.element.ASTElementType;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ValidationBuilder {

    private ValidationImpl validation;

    private Diagnostic.Kind kind;
    private String message;
    private Element element;
    private AnnotationMirror annotation;
    private AnnotationValue value;

    private ValidationBuilder(Diagnostic.Kind kind, String message){
        //empty utility class constructor
        this.kind = kind;
        this.message = message;
    }


    private class ValidationImpl implements Validation{

        private final Diagnostic.Kind kind;
        private final String message;
        private final Element element;
        private final AnnotationMirror annotation;
        private final AnnotationValue value;

        private ValidationImpl(Diagnostic.Kind kind, String message, Element element, AnnotationMirror annotation, AnnotationValue value) {
            this.kind = kind;
            this.message = message;
            this.element = element;
            this.annotation = annotation;
            this.value = value;
        }

        @Override
        public Diagnostic.Kind getKind() {
            return kind;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public Element getElement() {
            return element;
        }

        @Override
        public AnnotationMirror getAnnotation() {
            return annotation;
        }

        @Override
        public AnnotationValue getAnnotationValue() {
            return value;
        }
    }

    public static ValidationBuilder validator(Diagnostic.Kind kind, String message){
        return new ValidationBuilder(kind, message);
    }

    public ValidationBuilder element(ASTBase astBase){
        if(astBase instanceof ASTElementType){
            this.element = ((ASTElementType)astBase).getElement();
        }
        if(astBase instanceof ASTElementField){
            this.element = ((ASTElementField)astBase).getElement();
        }
        if(astBase instanceof ASTElementMethod){
            this.element = ((ASTElementMethod)astBase).getElement();
        }
        return this;
    }

    public ValidationBuilder annotation(ASTAnnotation annotation){
        if(annotation instanceof ASTElementAnnotation){
            this.annotation = ((ASTElementAnnotation)annotation).getAnnotationMirror();
        }
        return this;
    }


    public ValidationBuilder value(String propertyName){
        if(this.annotation != null){
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
                if (propertyName.equals(entry.getKey().getSimpleName().toString())) {
                    this.value = entry.getValue();
                    break;
                }
            }
        }
        return this;
    }

    public Validation build(){
        return new ValidationImpl(kind, message, element, annotation, value);
    }
}
