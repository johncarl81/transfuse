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

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ActivityFieldDelegateASTReference implements ActivityDelegateASTReference{

    private final ASTField astField;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public ActivityFieldDelegateASTReference(@Assisted ASTField astField, InvocationBuilder invocationBuilder) {
        this.astField = astField;
        this.invocationBuilder = invocationBuilder;
    }

    @Override
    public JExpression buildReference(TypedExpression rootExpression) {

        return invocationBuilder.buildFieldGet(astField.getASTType(),
                rootExpression.getType(),
                rootExpression.getExpression(),
                astField.getName(),
                astField.getAccessModifier());
    }
}
