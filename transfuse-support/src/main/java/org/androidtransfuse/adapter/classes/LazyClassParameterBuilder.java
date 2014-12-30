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
package org.androidtransfuse.adapter.classes;

import com.google.common.collect.ImmutableList;
import org.androidtransfuse.adapter.ASTType;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LazyClassParameterBuilder implements org.androidtransfuse.adapter.LazyTypeParameterBuilder {

    private final Class clazz;
    private final ASTClassFactory astClassFactory;
    private ImmutableList<ASTType> genericParameters = null;

    @Inject
    public LazyClassParameterBuilder(/*@Assisted*/ Class clazz,
                                     ASTClassFactory astClassFactory) {
        this.clazz = clazz;
        this.astClassFactory = astClassFactory;
    }

    public synchronized ImmutableList<ASTType> buildGenericParameters() {
        if (genericParameters == null) {
            genericParameters = innerBuildGenericParameters();
        }
        return genericParameters;
    }

    private ImmutableList<ASTType> innerBuildGenericParameters() {
        return ImmutableList.of(astClassFactory.getType(clazz));
    }
}
