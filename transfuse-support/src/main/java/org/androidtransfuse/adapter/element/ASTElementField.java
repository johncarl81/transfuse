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
package org.androidtransfuse.adapter.element;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAccessModifier;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTType;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Element specific implementation of the AST Field
 *
 * @author John Ericksen
 */
public class ASTElementField extends ASTElementBase implements ASTField {

    private final ASTTypeLazyLoader<Element> astTypeLoader;
    private final ASTAccessModifier modifier;
    private final VariableElement variableElement;

    public ASTElementField(VariableElement variableElement,
                           ASTTypeBuilderVisitor astTypeBuilderVisitor,
                           ASTAccessModifier modifier,
                           ImmutableSet<ASTAnnotation> annotations) {
        super(variableElement, annotations);
        this.variableElement = variableElement;
        this.modifier = modifier;
        this.astTypeLoader = new ElementASTTypeLazyLoader(variableElement, astTypeBuilderVisitor);
    }

    @Override
    public synchronized ASTType getASTType() {
        return astTypeLoader.getASTType();
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public Object getConstantValue() {
        return variableElement.getConstantValue();
    }

    @Override
    public boolean isTransient() {
        return variableElement.getModifiers().contains(Modifier.TRANSIENT);
    }

    public String toString(){
        return astTypeLoader.getASTType().getPackageClass().getClassName() + " " + variableElement.getSimpleName();
    }
}
