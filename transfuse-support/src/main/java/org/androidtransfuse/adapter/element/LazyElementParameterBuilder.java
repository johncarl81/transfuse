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

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.LazyTypeParameterBuilder;

import javax.inject.Inject;
import javax.lang.model.type.DeclaredType;

/**
 * @author John Ericksen
 */
public class LazyElementParameterBuilder implements LazyTypeParameterBuilder {

    private final DeclaredType declaredType;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private ImmutableList<ASTType> genericParameters = null;

    @Inject
    public LazyElementParameterBuilder(/*@Assisted*/ DeclaredType declaredType,
                                       ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.declaredType = declaredType;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    public synchronized ImmutableList<ASTType> buildGenericParameters() {
        if (genericParameters == null) {
            genericParameters = innerBuildGenericParameters();
        }
        return genericParameters;
    }

    public ImmutableList<ASTType> innerBuildGenericParameters() {
        return FluentIterable.from(declaredType.getTypeArguments())
                .transform(astTypeBuilderVisitor)
                .toList();
    }
}
