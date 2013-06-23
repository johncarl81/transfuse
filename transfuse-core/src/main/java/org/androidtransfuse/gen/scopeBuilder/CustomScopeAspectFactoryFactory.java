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
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class CustomScopeAspectFactoryFactory {

    private final TypedExpressionFactory typedExpressionFactory;
    private final ProviderGenerator providerGenerator;
    private final UniqueVariableNamer namer;
    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public CustomScopeAspectFactoryFactory(JCodeModel codeModel,
                                           ProviderGenerator providerGenerator,
                                           TypedExpressionFactory typedExpressionFactory,
                                           UniqueVariableNamer namer,
                                           ClassGenerationUtil generationUtil) {
        this.codeModel = codeModel;
        this.providerGenerator = providerGenerator;
        this.typedExpressionFactory = typedExpressionFactory;
        this.namer = namer;
        this.generationUtil = generationUtil;
    }

    public ScopeAspectFactory buildScopeBuilder(ASTType key) {
        return new CustomScopeAspectFactory(key, typedExpressionFactory, providerGenerator, namer, codeModel, generationUtil);
    }
}
