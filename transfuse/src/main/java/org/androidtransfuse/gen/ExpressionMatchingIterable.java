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

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.invocationBuilder.TypeInvocationHelper;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ExpressionMatchingIterable implements Iterable<JExpression> {

    private List<JExpression> matchedExpressions = new ArrayList<JExpression>();

    @Inject
    public ExpressionMatchingIterable(TypeInvocationHelper invocationHelper, @Assisted Map<InjectionNode, TypedExpression> variableMap, @Assisted List<InjectionNode> keys) {
        for (InjectionNode key : keys) {
            matchedExpressions.add(invocationHelper.getExpression(key.getASTType(), variableMap, key));
        }
    }

    @Override
    public Iterator<JExpression> iterator() {
        return matchedExpressions.iterator();
    }
}
