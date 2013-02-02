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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.model.InjectionNode;

/**
 * Analysis interface targeting the individual elements of an ASTType.
 *
 * @author John Ericksen
 */
public interface ASTAnalysis {

    /**
     * Analyze the given type.  Used during the class scanning phase to perform class level operations.
     *
     * @param injectionNode current injection node
     * @param astType       type
     * @param context       current context
     */
    void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context);

    /**
     * Analyze the given method.  Used during the class scanning phase to perform method level operations.
     *
     * @param injectionNode current injection node
     * @param concreteType  concrete type being analyzed.  This will be either the same type as represented by the
     *                      injection node or a super type under analysis.
     * @param astMethod     method
     * @param context       current context
     */
    void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context);

    /**
     * Analyze the given field.  Used during the class scanning phase to perform field level operations.
     *
     * @param injectionNode current injection node
     * @param concreteType  concrete type being analyzed.  This will be either the same type as represented by the
     *                      injection node or a super type under analysis.
     * @param astField      field
     * @param context       current context
     */
    void analyzeField(InjectionNode injectionNode, ASTType concreteType, ASTField astField, AnalysisContext context);
}
