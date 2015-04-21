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
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.IntentFactoryStrategyGenerator;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ParcelerPropertyBuilder implements PropertyBuilder{

    private final ClassGenerationUtil generationUtil;

    @Inject
    public ParcelerPropertyBuilder(ClassGenerationUtil generationUtil) {
        this.generationUtil = generationUtil;
    }

    @Override
    public JExpression buildReader() {
        return null;
    }

    @Override
    public JStatement buildWriter(JInvocation extras, String name, JVar extraParam) {
        JInvocation wrappedParcel = generationUtil.ref(IntentFactoryStrategyGenerator.PARCELS_NAME)
                .staticInvoke(IntentFactoryStrategyGenerator.WRAP_METHOD).arg(extraParam);

        return extras.invoke("putParcelable").arg(name).arg(wrappedParcel);
    }
}
