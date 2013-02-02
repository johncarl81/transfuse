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

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.astAnalyzer.RegistrationAspect;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import java.util.Map;

/**
 * @author John Ericksen
 */
public class ListenerRegistrationGenerator implements ExpressionVariableDependentGenerator {

    @Override
    public void generate(JDefinedClass definedClass, MethodDescriptor methodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor, JExpression scopesExpression) {

        JBlock block = methodDescriptor.getMethod().body();

        //add listener registration
        for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            if (injectionNodeJExpressionEntry.getKey().containsAspect(RegistrationAspect.class)) {
                RegistrationAspect registrationAspect = injectionNodeJExpressionEntry.getKey().getAspect(RegistrationAspect.class);

                for (RegistrationGenerator builder : registrationAspect.getRegistrationBuilders()) {
                    builder.build(definedClass, block, injectionNodeJExpressionEntry.getValue());
                }
            }
        }
    }
}
