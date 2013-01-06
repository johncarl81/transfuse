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
package org.androidtransfuse.model;

import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.JMethod;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class MethodDescriptorBuilder {

    private final JMethod codeModelMethod;
    private final ASTMethod astMethod;
    private final ImmutableMap.Builder<ASTType, TypedExpression> typeMapBuilder = ImmutableMap.builder();
    private final ImmutableMap.Builder<ASTParameter, TypedExpression> parameterMapBuilder = ImmutableMap.builder();


    public MethodDescriptorBuilder(JMethod codeModelMethod, ASTMethod astMethod) {
        this.codeModelMethod = codeModelMethod;
        this.astMethod = astMethod;
    }

    public void putType(ASTType astType, TypedExpression expression) {
        typeMapBuilder.put(astType, expression);
    }

    public void putParameter(ASTParameter astParameter, TypedExpression expression) {
        parameterMapBuilder.put(astParameter, expression);
    }

    public MethodDescriptor build() {
        return new MethodDescriptor(codeModelMethod, astMethod, parameterMapBuilder.build(), typeMapBuilder.build());
    }
}
