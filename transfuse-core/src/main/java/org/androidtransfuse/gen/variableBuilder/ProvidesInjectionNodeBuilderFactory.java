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
package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProvidesInjectionNodeBuilderFactory {

    private final Analyzer analyzer;
    private final InjectionPointFactory injectionNodeFactory;
    private final ProvidesVariableBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ProvidesInjectionNodeBuilderFactory(ProvidesVariableBuilderFactory variableInjectionBuilderFactory, InjectionPointFactory injectionNodeFactory, Analyzer analyzer) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionNodeFactory = injectionNodeFactory;
        this.analyzer = analyzer;
    }

    public ProvidesInjectionNodeBuilder buildProvidesBuilder(ASTType moduleType, ASTMethod providesMethod, ASTAnnotation scope){
        return new ProvidesInjectionNodeBuilder(moduleType, providesMethod, scope, analyzer, injectionNodeFactory, variableInjectionBuilderFactory);
    }
}
