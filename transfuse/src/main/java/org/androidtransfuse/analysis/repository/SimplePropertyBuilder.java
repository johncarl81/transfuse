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
package org.androidtransfuse.analysis.repository;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;

/**
 * @author John Ericksen
 */
public class SimplePropertyBuilder implements PropertyBuilder{

    private final String accessor;
    private final String mutator;

    public SimplePropertyBuilder(String accessor, String mutator) {
        this.accessor = accessor;
        this.mutator = mutator;
    }

    @Override
    public JExpression buildReader() {
        return null;
    }

    @Override
    public JStatement buildWriter(JExpression extras, String name, JVar extraParam) {
        return extras.invoke(mutator).arg(name).arg(extraParam);
    }
}
