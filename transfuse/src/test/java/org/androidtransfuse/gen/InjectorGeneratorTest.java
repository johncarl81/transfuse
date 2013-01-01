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
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class InjectorGeneratorTest {

    @Inject
    private InjectorGenerator injectorGenerator;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private ASTClassFactory astClassFactory;

    private Injector injector;

    public interface Injector {

        InjectorTarget getInjectorTarget();

        InjectorTarget getTargetWithDependency(InjectorTargetDependency dependency);
    }

    public static class InjectorTarget {

        @Inject
        public InjectorTargetDependency dependency;

        public InjectorTargetDependency getDependency() {
            return dependency;
        }
    }

    public static class InjectorTargetDependency {
    }

    @Before
    public void setUp() throws Exception {
        TransfuseTestInjector.inject(this);

        ASTType injectorType = astClassFactory.getType(Injector.class);

        JDefinedClass injectorDefinedClass = injectorGenerator.generate(injectorType);

        ClassLoader classLoader = codeGenerationUtil.build();

        Class<Injector> injectorClass = (Class<Injector>) classLoader.loadClass(injectorDefinedClass.fullName());

        injector = injectorClass.newInstance();
    }

    @Test
    public void testInjectionTarget() {
        InjectorTarget injectorTarget = injector.getInjectorTarget();
        assertNotNull(injectorTarget);
        assertNotNull(injectorTarget.getDependency());
    }

    @Test
    public void testInjectionTargetDependency() {
        InjectorTargetDependency dependency = new InjectorTargetDependency();

        InjectorTarget injectorTarget = injector.getTargetWithDependency(dependency);
        assertNotNull(injectorTarget);
        assertNotNull(injectorTarget.getDependency());
        assertEquals(dependency, injectorTarget.getDependency());
    }
}
