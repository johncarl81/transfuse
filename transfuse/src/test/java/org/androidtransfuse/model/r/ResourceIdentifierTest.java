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

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
@Bootstrap(test = true)
public class ResourceIdentifierTest {

    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType rInnerType;
    private ASTType otherInnerType;

    private static class RInnerType {
    }

    private static class OtherInnerType {
    }

    @Before
    public void setup() {
        Bootstraps.injectTest(this);

        rInnerType = astClassFactory.getType(RInnerType.class);
        otherInnerType = astClassFactory.getType(OtherInnerType.class);
    }

    @Test
    public void testEquality() {
        ResourceIdentifier valueIdentifier = new ResourceIdentifier(rInnerType, "value");
        ResourceIdentifier valueIdentifierDuplicate = new ResourceIdentifier(rInnerType, "value");
        ResourceIdentifier secondValueIdentifier = new ResourceIdentifier(rInnerType, "secondValue");
        ResourceIdentifier otherValueIdentifier = new ResourceIdentifier(otherInnerType, "value");

        assertTrue(valueIdentifier.equals(valueIdentifierDuplicate));
        assertTrue(valueIdentifierDuplicate.equals(valueIdentifierDuplicate));
        assertEquals(valueIdentifier.hashCode(), valueIdentifierDuplicate.hashCode());
        assertEquals(valueIdentifier.getName(), valueIdentifierDuplicate.getName());
        assertEquals(valueIdentifier.getRInnerType(), valueIdentifierDuplicate.getRInnerType());

        assertFalse(valueIdentifier.equals(secondValueIdentifier));
        assertFalse(secondValueIdentifier.equals(valueIdentifierDuplicate));
        assertNotSame(valueIdentifier.hashCode(), secondValueIdentifier.hashCode());

        assertFalse(valueIdentifier.equals(otherValueIdentifier));
        assertFalse(otherValueIdentifier.equals(valueIdentifierDuplicate));
        assertNotSame(valueIdentifier.hashCode(), valueIdentifierDuplicate.hashCode());

        assertFalse(valueIdentifier.equals(new Object()));

    }
}
