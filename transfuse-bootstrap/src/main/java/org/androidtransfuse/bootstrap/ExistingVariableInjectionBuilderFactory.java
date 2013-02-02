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
package org.androidtransfuse.bootstrap;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.ExceptionWrapper;
import org.androidtransfuse.gen.GeneratorFactory2;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;

/**
 * @author John Ericksen
 */
public class ExistingVariableInjectionBuilderFactory {

    private final InvocationBuilder injectionInvocationBuilder;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final TypedExpressionFactory typedExpressionFactory;
    private final ExceptionWrapper exceptionWrapper;
    private final GeneratorFactory2 generatorFactory;

    public ExistingVariableInjectionBuilderFactory(InvocationBuilder injectionInvocationBuilder,
                                                   InjectionExpressionBuilder injectionExpressionBuilder,
                                                   TypedExpressionFactory typedExpressionFactory,
                                                   ExceptionWrapper exceptionWrapper,
                                                   GeneratorFactory2 generatorFactory) {
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
        this.exceptionWrapper = exceptionWrapper;
        this.generatorFactory = generatorFactory;
    }

    public ExistingVariableInjectionBuilder buildVariableBuilder(JExpression expression){

        return new ExistingVariableInjectionBuilder(expression,
                injectionInvocationBuilder,
                injectionExpressionBuilder,
                typedExpressionFactory,
                exceptionWrapper,
                generatorFactory);
    }
}
