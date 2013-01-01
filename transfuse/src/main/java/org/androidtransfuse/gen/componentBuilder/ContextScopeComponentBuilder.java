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

import com.sun.codemodel.*;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.ConcurrentDoubleLockingScope;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.scope.Scope;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ContextScopeComponentBuilder implements ExpressionVariableDependentGenerator{

    private final JCodeModel codeModel;
    private final UniqueVariableNamer namer;

    @Inject
    public ContextScopeComponentBuilder(JCodeModel codeModel, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.namer = namer;
    }

    @Override
    public void generate(JDefinedClass definedClass, MethodDescriptor methodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {
        //setup context scope
        definedClass._implements(ContextScopeHolder.class);

        //scope variable
        JFieldVar scopeField = definedClass.field(JMod.PRIVATE, Scope.class, namer.generateName(Scope.class),
                JExpr._new(codeModel.ref(ConcurrentDoubleLockingScope.class)));

        //method
        JMethod getScope = definedClass.method(JMod.PUBLIC, Scope.class, ContextScopeHolder.GET_SCOPE);
        getScope.body()._return(scopeField);
    }
}
