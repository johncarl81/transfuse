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

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.util.InjectorRepository;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class InjectorsGeneratorTest {

    @Inject
    private InjectorsGenerator injectorsGenerator;
    @Inject
    private InjectorGenerator injectorGenerator;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;

    private Class injectorsClass;

    public interface Injector {
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
        TransfuseTestInjector.inject(this);

        ASTType injectorType = astClassFactory.getType(Injector.class);
        JDefinedClass injectorGeneratedClass = injectorGenerator.generate(injectorType);

        JDefinedClass injectorsDefinedClass = injectorsGenerator.generateInjectors(
                Collections.<Provider<ASTType>, JDefinedClass>singletonMap(
                        new SingletonProvider<ASTType>(injectorType), injectorGeneratedClass));

        ClassLoader classLoader = codeGenerationUtil.build();

        injectorsClass = classLoader.loadClass(injectorsDefinedClass.fullName());
    }

    @Test
    public void test() throws Exception {
        InjectorRepository injector = (InjectorRepository) injectorsClass.newInstance();
        assertNotNull(injectorsClass.getMethod("get", Class.class).invoke(injector, Injector.class));
    }
}
