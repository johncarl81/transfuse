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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Element specific implementation of the AST Constructor
 *
 * @author John Ericksen
 */
public class ASTElementConstructor extends ASTElementBase implements ASTConstructor {

    private final ImmutableList<ASTParameter> parameters;
    private final ASTAccessModifier modifier;
    private final ImmutableSet<ASTType> throwsTypes;

    public ASTElementConstructor(ExecutableElement executableElement,
                                 ImmutableList<ASTParameter> parameters,
                                 ASTAccessModifier modifier,
                                 ImmutableSet<ASTAnnotation> annotations,
                                 ImmutableSet<ASTType> throwsTypes) {
        super(executableElement, annotations);
        this.parameters = parameters;
        this.modifier = modifier;
        this.throwsTypes = throwsTypes;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public ImmutableSet<ASTType> getThrowsTypes() {
        return throwsTypes;
    }

    public String toString(){
        return getElement().getEnclosingElement().getSimpleName() + "(" + Joiner.on(", ").join(parameters) + ")";
    }
}
