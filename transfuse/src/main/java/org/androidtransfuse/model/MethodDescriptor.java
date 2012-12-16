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
package org.androidtransfuse.model;

import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.JMethod;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.Map;

/**
 * @author John Ericksen
 */
public class MethodDescriptor {

    private final JMethod method;
    private final ImmutableMap<ASTParameter, TypedExpression> parameterMap;
    private final ImmutableMap<ASTType, TypedExpression> typeMap;
    private final ASTMethod astMethod;

    public MethodDescriptor(JMethod method, ASTMethod astMethod, ImmutableMap<ASTParameter, TypedExpression> parameterMap, ImmutableMap<ASTType, TypedExpression> typeMap) {
        this.method = method;
        this.astMethod = astMethod;
        this.parameterMap = parameterMap;
        this.typeMap = typeMap;
    }

    public JMethod getMethod() {
        return method;
    }

    public TypedExpression getParameter(ASTParameter astParameter) {
        return parameterMap.get(astParameter);
    }

    public ASTMethod getASTMethod() {
        return astMethod;
    }

    public Map<ASTParameter, TypedExpression> getParameters() {
        return parameterMap;
    }

    public Map<ASTType, TypedExpression> getTypeMap() {
        return typeMap;
    }

    public TypedExpression getExpression(ASTType astType) {
        return typeMap.get(astType);
    }
}
