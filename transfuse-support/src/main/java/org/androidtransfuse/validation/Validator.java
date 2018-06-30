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
package org.androidtransfuse.validation;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.adapter.element.ASTElementAnnotation;
import org.androidtransfuse.adapter.element.ElementHolder;

import javax.annotation.processing.Messager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
@Singleton
public class Validator {

    public static final String LOG_PREPEND = "logPrepend";

    private final String prepend;
    private final Messager messager;
    private final Set<ASTBase> suppressWarnings = new HashSet<ASTBase>();
    private boolean inError = false;

    @Inject
    public Validator(@Named(LOG_PREPEND) String prepend, Messager messager){
        this.prepend = prepend;
        this.messager = messager;
    }

    public ValidationBuilder error(String message){
        inError = true;
        return new ValidationBuilder(Diagnostic.Kind.ERROR, prepend + message);
    }

    public ValidationBuilder warn(String message){
        return new ValidationBuilder(Diagnostic.Kind.WARNING, prepend + message);
    }

    public final class ValidationBuilder{
        private Diagnostic.Kind kind;
        private String message;
        private Element element;
        private AnnotationMirror annotation;
        private AnnotationValue value;
        private boolean suppress = false;

        private ValidationBuilder(Diagnostic.Kind kind, String message){
            //empty utility class constructor
            this.kind = kind;
            this.message = message;
        }

        public ValidationBuilder element(ASTBase astBase){
            if(astBase instanceof ElementHolder){
                this.element = ((ElementHolder)astBase).getElement();
            }
            if(kind == Diagnostic.Kind.WARNING && suppressWarnings.contains(astBase)) {
                this.suppress = true;
            }
            return this;
        }

        public ValidationBuilder annotation(ASTAnnotation annotation){
            if(annotation instanceof ASTElementAnnotation){
                this.annotation = ((ASTElementAnnotation)annotation).getAnnotationMirror();
            }
            return this;
        }


        public ValidationBuilder parameter(String propertyName){
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

        public void build(){
            inError = kind == Diagnostic.Kind.ERROR;
            if(element != null){
                if(!suppress) {
                    if (annotation != null) {
                        if (value != null) {
                            messager.printMessage(kind,
                                    message,
                                    element,
                                    annotation,
                                    value);
                        } else {
                            messager.printMessage(kind,
                                    message,
                                    element,
                                    annotation);
                        }
                    } else {
                        messager.printMessage(kind,
                                message,
                                element);
                    }
                }
            }
            else{
                messager.printMessage(kind,
                        message);
            }
        }
    }

    public boolean isInError() {
        return inError;
    }

    public void addSuppression(ASTBase ast) {
        this.suppressWarnings.add(ast);
    }
}
