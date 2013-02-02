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

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
@Bootstrap(test = true)
public class RBuilderTest {

    @Inject
    private RBuilder rBuilder;
    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType idInnerType;

    public class RTest {
        public static final int id1 = 1;
    }

    @Before
    public void setup() {
        Bootstraps.injectTest(this);

        idInnerType = astClassFactory.getType(RTest.class);
    }

    @Test
    public void testBuild() {
        RResourceMapping rResourceMapping = rBuilder.buildR(Collections.singleton(idInnerType));

        ResourceIdentifier resourceIdentifier = rResourceMapping.getResourceIdentifier(RTest.id1);

        assertEquals("id1", resourceIdentifier.getName());
        assertEquals(idInnerType, resourceIdentifier.getRInnerType());
    }
}
