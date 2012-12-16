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

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
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
        TransfuseTestInjector.inject(this);

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
