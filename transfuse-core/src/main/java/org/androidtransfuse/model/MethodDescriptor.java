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
package org.androidtransfuse.model;

import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.JMethod;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class MethodDescriptor {

    private final JMethod method;
    private final ImmutableMap<ASTParameter, TypedExpression> parameterMap;
    private final ImmutableMap<ASTType, TypedExpression> querymap;
    private final ASTMethod astMethod;

    public MethodDescriptor(JMethod method, ASTMethod astMethod, ImmutableMap<ASTParameter, TypedExpression> parameterMap, ImmutableMap<ASTType, TypedExpression> typeMap) {
        this.method = method;
        this.astMethod = astMethod;
        this.parameterMap = parameterMap;

        ImmutableMap.Builder<ASTType, TypedExpression> queryBuilder = ImmutableMap.builder();

        Set<ASTType> duplicateTypes = new HashSet<ASTType>();

        duplicateTypes.addAll(typeMap.keySet());

        queryBuilder.putAll(typeMap);
        for (Map.Entry<ASTParameter, TypedExpression> parameterEntry : parameterMap.entrySet()) {
            if(!duplicateTypes.contains(parameterEntry.getKey().getASTType())){
                queryBuilder.put(parameterEntry.getKey().getASTType(), parameterEntry.getValue());
                duplicateTypes.add(parameterEntry.getKey().getASTType());
            }
        }

        this.querymap = queryBuilder.build();
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

    //todo: move this to a properly build module repository
    public TypedExpression getExpression(ASTType astType) {
        return querymap.get(astType);
    }
}
