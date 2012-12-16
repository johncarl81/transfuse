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

import javax.lang.model.element.Element;

/**
 * AST Type loader, lazily loading the getASTType() return value from the abstract buildASTType() method
 *
 * @author John Ericksen
 */
public abstract class ASTTypeLazyLoader<T extends Element> {

    private ASTType astType = null;
    private final T element;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;

    public ASTTypeLazyLoader(T element, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.element = element;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    /**
     * Lazily builds the ASTType for the given element
     *
     * @return ASTType
     */
    public synchronized ASTType getASTType() {
        if (astType == null) {
            astType = buildASTType(element, astTypeBuilderVisitor);
        }
        return astType;
    }

    protected abstract ASTType buildASTType(T element, ASTTypeBuilderVisitor astTypeBuilderVisitor);
}
