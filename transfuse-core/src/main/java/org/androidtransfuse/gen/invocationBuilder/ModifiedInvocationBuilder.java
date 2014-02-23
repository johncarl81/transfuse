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
package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.adapter.ASTConstructor;
import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.model.TypedExpression;

import java.util.List;

/**
 * @author John Ericksen
 */
public interface ModifiedInvocationBuilder {

    JExpression buildConstructorCall(ASTConstructor constructor, ASTType type, List<? extends JExpression> parameters);

    JExpression buildFieldGet(boolean cast, ASTField field, TypedExpression variable);

    JStatement buildFieldSet(boolean cast, ASTField field, TypedExpression inputExpression, TypedExpression containingExpression);

    JInvocation buildMethodCall(boolean cast, ASTMethod method, List<? extends JExpression> parameters, TypedExpression expression);
}
