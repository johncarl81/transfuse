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
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@Bootstrap
public class FactoryGeneratorTest {

    @Inject
    private FactoryGenerator factoryGenerator;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private ASTClassFactory astClassFactory;

    private Factory factory;

    public static class FactoryTargetDependency {}

    public interface Factory {
        FactoryTarget getFactoryTarget();
        FactoryTarget getTargetWithDependency(FactoryTargetDependency dependency);
    }

    public static class FactoryTarget {
        @Inject
        public FactoryTargetDependency dependency;
        public FactoryTargetDependency getDependency() {
            return dependency;
        }
    }

    @Before
    public void setup() throws Exception {
        Bootstraps.inject(this);

        ASTType factoryType = astClassFactory.getType(Factory.class);

        JDefinedClass factoryDefinedClass = factoryGenerator.generate(factoryType);

        ClassLoader classLoader = codeGenerationUtil.build();

        Class<Factory> factoryClass = (Class<Factory>) classLoader.loadClass(factoryDefinedClass.fullName());

        factory = factoryClass.newInstance();
    }

    @Test
    public void testInjectionTarget() {
        FactoryTarget factoryTarget = factory.getFactoryTarget();
        assertNotNull(factoryTarget);
        assertNotNull(factoryTarget.getDependency());
    }

    @Test
    public void testInjectionTargetDependency() {
        FactoryTargetDependency dependency = new FactoryTargetDependency();

        FactoryTarget factoryTarget = factory.getTargetWithDependency(dependency);
        assertNotNull(factoryTarget);
        assertNotNull(factoryTarget.getDependency());
        assertEquals(dependency, factoryTarget.getDependency());
    }
}
