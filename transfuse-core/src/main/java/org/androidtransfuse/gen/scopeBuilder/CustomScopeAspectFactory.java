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
package org.androidtransfuse.gen.scopeBuilder;

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableBuilder.CustomScopeVariableBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class CustomScopeAspectFactory implements ScopeAspectFactory {

    private final ASTType scopeKey;
    private final TypedExpressionFactory typedExpressionFactory;
    private final ProviderGenerator providerGenerator;
    private final UniqueVariableNamer namer;
    private final JCodeModel codeModel;

    public CustomScopeAspectFactory(ASTType scopeKey, TypedExpressionFactory typedExpressionFactory, ProviderGenerator providerGenerator, UniqueVariableNamer namer, JCodeModel codeModel) {
        this.scopeKey = scopeKey;
        this.typedExpressionFactory = typedExpressionFactory;
        this.providerGenerator = providerGenerator;
        this.namer = namer;
        this.codeModel = codeModel;
    }

    @Override
    public ScopeAspect buildAspect(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {

        return new ScopeAspect(new CustomScopeVariableBuilder(scopeKey, typedExpressionFactory, providerGenerator, codeModel, namer));
    }
}
