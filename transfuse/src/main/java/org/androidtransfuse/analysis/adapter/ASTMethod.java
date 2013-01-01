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
package org.androidtransfuse.analysis.adapter;

import java.util.List;

/**
 * Abstract Syntax Tree Method node
 *
 * @author John Ericksen
 */
public interface ASTMethod extends ASTBase {

    /**
     * Supplies all parameters of this method
     *
     * @return method parameters
     */
    List<ASTParameter> getParameters();

    /**
     * Supplies the return type of this method
     *
     * @return return type
     */
    ASTType getReturnType();

    /**
     * Supplies the access modifier for this method.
     *
     * @return method access modifier
     */
    ASTAccessModifier getAccessModifier();

    /**
     * Supplies all throws associated with this method
     *
     * @return throw types
     */
    List<ASTType> getThrowsTypes();
}
