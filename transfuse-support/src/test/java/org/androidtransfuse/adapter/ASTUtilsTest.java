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
package org.androidtransfuse.adapter;

import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class ASTUtilsTest {

    public static class A extends B{}
    public static class B extends C implements D{}
    public interface D{}
    public static class C implements E{}
    public interface E extends F, G {}
    public interface F {}
    public interface G {}
    public static class H{}
    public interface I{}

    private ASTType a;
    private ASTType b;
    private ASTType c;
    private ASTType d;
    private ASTType e;
    private ASTType f;
    private ASTType g;
    private ASTType h;
    private ASTType i;
    private ASTType objectType;

    private ASTUtils astUtils;

    @Before
    public void setup() {
        ASTClassFactory classFactory = new ASTClassFactory();
        a = classFactory.getType(A.class);
        b = classFactory.getType(B.class);
        c = classFactory.getType(C.class);
        d = classFactory.getType(D.class);
        e = classFactory.getType(E.class);
        f = classFactory.getType(F.class);
        g = classFactory.getType(G.class);
        h = classFactory.getType(H.class);
        i = classFactory.getType(I.class);
        objectType = classFactory.getType(Object.class);

        astUtils = ASTUtils.getInstance();
    }

    @Test
    public void testExtends() {
        assertTrue(astUtils.inherits(a, b));
        assertTrue(astUtils.inherits(a, c));
        assertFalse(astUtils.inherits(a, h));
        assertTrue(astUtils.inherits(a, objectType));
        assertFalse(astUtils.inherits(b, a));
        assertFalse(astUtils.inherits(c, a));
    }

    @Test
    public void testInherits() {
        assertTrue(astUtils.inherits(a, d));
        assertTrue(astUtils.inherits(a, e));
        assertTrue(astUtils.inherits(a, f));
        assertTrue(astUtils.inherits(a, g));
        assertTrue(astUtils.inherits(e, f));
        assertTrue(astUtils.inherits(e, g));
        assertFalse(astUtils.inherits(e, h));
        assertFalse(astUtils.inherits(d, e));
        assertFalse(astUtils.inherits(a, i));
    }

    @Test
    public void testNull() {
        assertTrue(astUtils.inherits(a, null));
        assertFalse(astUtils.inherits(null, null));
    }

    @Test
    public void testSelf() {
        assertTrue(astUtils.inherits(a, a));
    }
}