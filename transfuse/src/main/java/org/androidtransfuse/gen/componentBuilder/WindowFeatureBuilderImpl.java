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
import com.sun.codemodel.JExpr;

import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class WindowFeatureBuilderImpl implements WindowFeatureBuilder {

    private final Integer[] values;

    public WindowFeatureBuilderImpl(Integer[] values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    @Override
    public void builderWindowFeatureCall(JDefinedClass definedClass, JBlock block) {

        for (int value : values) {
            block.invoke("requestWindowFeature").arg(JExpr.lit(value));
        }
    }
}
