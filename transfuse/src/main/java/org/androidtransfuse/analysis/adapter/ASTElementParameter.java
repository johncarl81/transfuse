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
package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableCollection;

import javax.lang.model.element.Element;

/**
 * Element specific implementation of a AST method parameter
 *
 * @author John Ericksen
 */
public class ASTElementParameter extends ASTElementBase implements ASTParameter {

    private final ASTTypeLazyLoader<Element> astTypeLoader;

    public ASTElementParameter(Element element,
                               ASTTypeBuilderVisitor astTypeBuilderVisitor,
                               ImmutableCollection<ASTAnnotation> annotations) {
        super(element, annotations);
        this.astTypeLoader = new ElementASTTypeLazyLoader(element, astTypeBuilderVisitor);
    }

    @Override
    public synchronized ASTType getASTType() {
        return astTypeLoader.getASTType();
    }
}
