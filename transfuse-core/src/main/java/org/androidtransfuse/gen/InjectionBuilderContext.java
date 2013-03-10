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

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionBuilderContext {

    private final Map<InjectionNode, TypedExpression> variableMap;
    private final JBlock block;
    private final JDefinedClass definedClass;
    private final JExpression scopeVar;
    private final Map<InjectionNode, TypedExpression> proxyLoad = new HashMap<InjectionNode, TypedExpression>();

    @Inject
    public InjectionBuilderContext(/*@Assisted*/ JBlock block,
                                   /*@Assisted*/ JDefinedClass definedClass,
                                   /*@Assisted*/ JExpression scopeVar,
                                   /*@Assisted*/ Map<InjectionNode, TypedExpression> variableMap) {
        this.block = block;
        this.definedClass = definedClass;
        this.variableMap = variableMap;
        this.scopeVar = scopeVar;
    }


    public Map<InjectionNode, TypedExpression> getVariableMap() {
        return variableMap;
    }

    public JBlock getBlock() {
        return block;
    }

    public JDefinedClass getDefinedClass() {
        return definedClass;
    }

    public Map<InjectionNode, TypedExpression> getProxyLoad() {
        return proxyLoad;
    }

    public JExpression getScopeVar() {
        return scopeVar;
    }
}
