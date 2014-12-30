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

import com.google.common.base.Function;
import org.androidtransfuse.adapter.ASTType;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collection;

import static com.google.common.collect.Collections2.transform;

/**
 * @author John Ericksen
 */
public class ReloadableASTClassFactory implements Function<Class, Provider<ASTType>> {

    private final ASTClassFactory astClassFactory;

    @Inject
    public ReloadableASTClassFactory(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    public Collection<Provider<ASTType>> buildProviders(Collection<? extends Class> classes){
        return transform(classes, this);
    }

    @Override
    public Provider<ASTType> apply(final Class clazz) {
        return new ASTClassProvider(astClassFactory.getType(clazz));
    }

    public class ASTClassProvider implements Provider<ASTType>{
        ASTType type;

        public ASTClassProvider(ASTType type) {
            this.type = type;
        }

        @Override
        public ASTType get() {
            return type;
        }
    }
}
