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

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class StaticInvocationVariableBuilder implements DependentVariableBuilder{

    private final Class invocationTarget;
    private final String staticInvocation;
    private final JCodeModel codeModel;

    @Inject
    public StaticInvocationVariableBuilder(/*@Assisted*/ Class invocationTarget,
                                           /*@Assisted*/ String staticInvocation,
                                           JCodeModel codeModel) {
        this.invocationTarget = invocationTarget;
        this.staticInvocation = staticInvocation;
        this.codeModel = codeModel;
    }

    @Override
    public JExpression buildVariable(JExpression dependencyExpression) {
        return codeModel.ref(invocationTarget).staticInvoke(staticInvocation).arg(dependencyExpression);
    }
}
