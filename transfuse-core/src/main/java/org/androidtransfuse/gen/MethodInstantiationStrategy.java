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
package org.androidtransfuse.gen;

import com.sun.codemodel.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class MethodInstantiationStrategy implements InstantiationStrategy {

    private final Map<JDefinedClass, JExpression> variables = new HashMap<JDefinedClass, JExpression>();
    private final JExpression scopesVar;
    private final UniqueVariableNamer namer;
    private final JBlock block;

    @Inject
    public MethodInstantiationStrategy(
            /*@Assisted*/ JBlock block,
            /*@Assisted*/ JExpression scopes,
            UniqueVariableNamer namer) {
        this.block = block;
        this.scopesVar = scopes;
        this.namer = namer;
    }

    @Override
    public JExpression instantiate(JDefinedClass providerClass) {
        if(!variables.containsKey(providerClass)){
            JVar variable = block.decl(providerClass, namer.generateName(providerClass));
            block.assign(variable, JExpr._new(providerClass).arg(scopesVar));
            variables.put(providerClass, variable);
        }
        return variables.get(providerClass);
    }
}
