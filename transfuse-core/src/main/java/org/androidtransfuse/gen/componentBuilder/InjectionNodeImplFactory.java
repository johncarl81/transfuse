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
package org.androidtransfuse.gen.componentBuilder;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.gen.variableBuilder.VariableFactoryBuilderFactory2;
import org.androidtransfuse.util.QualifierPredicate;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectionNodeImplFactory {

    private final InjectionPointFactory injectionPointFactory;
    private final VariableFactoryBuilderFactory2 injectionBindingBuilder;
    private final QualifierPredicate qualifierPredicate;

    @Inject
    public InjectionNodeImplFactory(InjectionPointFactory injectionPointFactory, VariableFactoryBuilderFactory2 injectionBindingBuilder, QualifierPredicate qualifierPredicate) {
        this.injectionPointFactory = injectionPointFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.qualifierPredicate = qualifierPredicate;
    }

    public InjectionNodeFactory buildInjectionNodeFactory(ASTType returnType, AnalysisContext context) {
        return new InjectionNodeFactoryImpl(returnType, context, injectionPointFactory, injectionBindingBuilder, qualifierPredicate);
    }
}
