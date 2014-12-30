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
package org.androidtransfuse.analysis;

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.MethodSignature;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
@Bootstrap
public class MethodSignatureTest {

    @Inject
    private ASTClassFactory astClassFactory;
    private ASTMethod methodOne;
    private ASTMethod methodTwo;
    private ASTMethod methodThree;

    public class MethodSignatureTarget {
        public void methodOne(String arg) {
        }

        public void methodTwo(int[] args) {
        }

        public long methodThree() {
            return 0;
        }
    }

    @Before
    public void setup() {
        Bootstraps.inject(this);

        ASTType targetType = astClassFactory.getType(MethodSignatureTarget.class);

        Iterator<ASTMethod> iterator = targetType.getMethods().iterator();

        methodOne = iterator.next();
        methodTwo = iterator.next();
        methodThree = iterator.next();
    }

    @Test
    public void testEquality() {

        assertEquals(new MethodSignature(methodOne), new MethodSignature(methodOne));
        assertFalse(new MethodSignature(methodOne).equals(new MethodSignature(methodTwo)));
        assertFalse(new MethodSignature(methodTwo).equals(new MethodSignature(methodThree)));
    }

    @Test
    public void testSet() {
        Set<MethodSignature> signatures = new HashSet<MethodSignature>();

        signatures.add(new MethodSignature(methodOne));

        assertTrue(signatures.contains(new MethodSignature(methodOne)));
        assertFalse(signatures.contains(new MethodSignature(methodTwo)));
    }

    @Test
    public void testEmptyVarargs(){
        new MethodSignature("test");
    }
}
