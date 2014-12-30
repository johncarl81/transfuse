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
package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.Repository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@Bootstrap
public class FactoriesGeneratorTest {

    @Inject
    private FactoriesGenerator factoriesGenerator;
    @Inject
    private FactoryGenerator factoryGenerator;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;

    private Class factoryClass;

    public interface Factory {
    }

    private class SingletonProvider<T> implements Provider<T> {
        T value;

        private SingletonProvider(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }
    }

    @Before
    public void setUp() throws Exception {
        Bootstraps.inject(this);

        ASTType factoryType = astClassFactory.getType(Factory.class);
        JDefinedClass factoryGeneratedClass = factoryGenerator.generate(factoryType);

        JDefinedClass factoriesDefinedClass = factoriesGenerator.generate(
                Collections.<Provider<ASTType>, JDefinedClass>singletonMap(
                        new SingletonProvider<ASTType>(factoryType), factoryGeneratedClass));

        ClassLoader classLoader = codeGenerationUtil.build();

        factoryClass = classLoader.loadClass(factoriesDefinedClass.fullName());
    }

    @Test
    @Ignore
    public void test() throws Exception {
        Repository factoryRepository = (Repository) factoryClass.newInstance();
        assertNotNull(factoryClass.getMethod("get", Class.class).invoke(factoryRepository, Factory.class));
        assertNotNull(factoryClass.getMethod("get", Class.class, Scopes.class).invoke(factoryRepository, Factory.class, new Scopes()));
    }
}
