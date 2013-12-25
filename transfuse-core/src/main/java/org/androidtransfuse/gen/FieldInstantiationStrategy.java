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
public class FieldInstantiationStrategy implements InstantiationStrategy {

    private final Map<JDefinedClass, JExpression> fields = new HashMap<JDefinedClass, JExpression>();
    private final JExpression scopesVar;
    private final UniqueVariableNamer namer;
    private final JDefinedClass definedClass;
    private final JBlock constructorBlock;

    @Inject
    public FieldInstantiationStrategy(
            /*@Assisted*/ JDefinedClass definedClass,
            /*@Assisted*/ JBlock constructorBlock,
            /*@Assisted*/ JExpression scopes,
            UniqueVariableNamer namer

    ) {
        this.definedClass = definedClass;
        this.constructorBlock = constructorBlock;
        this.scopesVar = scopes;
        this.namer = namer;
    }

    @Override
    public JExpression instantiate(JDefinedClass providerClass) {
        if(!fields.containsKey(providerClass)){
            JFieldVar field = definedClass.field(JMod.PRIVATE, providerClass, namer.generateName(providerClass));
            constructorBlock.assign(field, JExpr._new(providerClass).arg(scopesVar));
            fields.put(providerClass, field);
        }
        return fields.get(providerClass);
    }
}
