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
package org.androidtransfuse.model.r;

import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author John Ericksen
 */
@Bootstrap(test = true)
public class RResourceCompositeTest {

    private static final int RESOURCE_THAT_DOESNT_EXIST = 12345;

    @Inject
    private ASTClassFactory astClassFactory;
    private RResourceComposite resourceComposite;
    private RResourceMapping resourceOne;
    private RResourceMapping resourceTwo;

    private static class RTest {
        public static final class one {
            public static final int id1 = 0x7f070003;
        }

        public static final class two {
            public static final int id2 = 0x7f070004;
        }
    }

    @Before
    public void setup() {
        Bootstraps.injectTest(this);

        resourceOne = new RResourceMapping();
        resourceOne.addResource(astClassFactory.getType(RTest.one.class), "id1", RTest.one.id1);

        resourceTwo = new RResourceMapping();
        resourceTwo.addResource(astClassFactory.getType(RTest.two.class), "id2", RTest.two.id2);

        resourceComposite = new RResourceComposite(resourceOne, resourceTwo);
    }

    @Test
    public void testComposite() {
        ResourceIdentifier resourceIdOne = resourceComposite.getResourceIdentifier(RTest.one.id1);
        ResourceIdentifier resourceIdTwo = resourceComposite.getResourceIdentifier(RTest.two.id2);

        assertEquals(resourceOne.getResourceIdentifier(RTest.one.id1), resourceIdOne);
        assertEquals(resourceTwo.getResourceIdentifier(RTest.two.id2), resourceIdTwo);
        assertNull(resourceComposite.getResourceIdentifier(RESOURCE_THAT_DOESNT_EXIST));
    }

    @Test
    public void testNullInput() {
        RResourceComposite rResourceComposite = new RResourceComposite(null, resourceOne);

        ResourceIdentifier resourceIdOne = rResourceComposite.getResourceIdentifier(RTest.one.id1);
        assertEquals(resourceOne.getResourceIdentifier(RTest.one.id1), resourceIdOne);
        assertNull(rResourceComposite.getResourceIdentifier(RTest.two.id2));
    }

    @Test
    public void testEmptyResourceComposite() {
        RResourceComposite nullResourceComposite = new RResourceComposite();

        assertNull(nullResourceComposite.getResourceIdentifier(RTest.one.id1));
    }
}
