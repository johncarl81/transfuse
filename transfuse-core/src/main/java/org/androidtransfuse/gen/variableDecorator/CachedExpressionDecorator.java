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
package org.androidtransfuse.gen.variableDecorator;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class CachedExpressionDecorator extends VariableExpressionBuilderDecorator {

    @Inject
    public CachedExpressionDecorator(@Assisted @Named("variableExpressionBuilder") VariableExpressionBuilder decorated) {
        super(decorated);
    }

    @Override
    public TypedExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        Map<InjectionNode, TypedExpression> variableMap = injectionBuilderContext.getVariableMap();
        if (!variableMap.containsKey(injectionNode)) {
            variableMap.put(injectionNode, getDecorated().buildVariableExpression(injectionBuilderContext, injectionNode));
        }
        return variableMap.get(injectionNode);
    }
}
