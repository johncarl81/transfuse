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
package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.ClassGenerationUtil;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class StaticInvocationVariableBuilder implements DependentVariableBuilder{

    private final ASTType invocationTarget;
    private final String staticInvocation;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public StaticInvocationVariableBuilder(/*@Assisted*/ ASTType invocationTarget,
                                           /*@Assisted*/ String staticInvocation,
                                           ClassGenerationUtil generationUtil) {
        this.invocationTarget = invocationTarget;
        this.staticInvocation = staticInvocation;
        this.generationUtil = generationUtil;
    }

    @Override
    public JExpression buildVariable(JExpression dependencyExpression) {
        return generationUtil.ref(invocationTarget).staticInvoke(staticInvocation).arg(dependencyExpression);
    }
}
