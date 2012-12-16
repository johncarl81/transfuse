/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.model.r;

import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.Collection;

/**
 * @author John Ericksen
 */
public class RBuilder {

    public RResourceMapping buildR(Collection<? extends ASTType> rInnerTypes) {

        RResourceMapping resourceMapping = new RResourceMapping();

        for (ASTType rInnerType : rInnerTypes) {

            for (ASTField idField : rInnerType.getFields()) {
                resourceMapping.addResource(rInnerType, idField.getName(), (Integer) idField.getConstantValue());
            }
        }

        return resourceMapping;
    }
}
