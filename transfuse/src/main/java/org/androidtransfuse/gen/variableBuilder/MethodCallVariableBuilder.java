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

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class MethodCallVariableBuilder implements DependentVariableBuilder{

    private final String methodName;
    private final ImmutableList<JExpression> arguments;

    @Inject
    public MethodCallVariableBuilder(@Assisted String methodName,
                                     @Assisted ImmutableList<JExpression> arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    public JExpression buildVariable(JExpression dependencyExpression) {
        JInvocation invocation = dependencyExpression.invoke(methodName);

        for (JExpression argument : arguments) {
            invocation.arg(argument);
        }

        return invocation;
    }
}
