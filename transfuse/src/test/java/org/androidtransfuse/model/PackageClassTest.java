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
package org.androidtransfuse.model;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
public class PackageClassTest {

    private static final String PACKAGE_NAME = "org.test";
    private static final String CLASS_NAME = "ClassName";
    private static final String FULLY_QUALIFIED_NAME = PACKAGE_NAME + "." + CLASS_NAME;
    private static final String DOT_JAVA = ".java";
    private static final String TEST_NAME = "Name";

    public static class Inner$Stuff {
    }

    @Test
    public void testClassInput() {
        PackageClass packageClass = new PackageClass(PackageClassTest.class);

        assertEquals("org.androidtransfuse.model.PackageClassTest", packageClass.getFullyQualifiedName());
        assertEquals("PackageClassTest", packageClass.getClassName());
        assertEquals("org.androidtransfuse.model", packageClass.getPackage());
        assertEquals("org.androidtransfuse.model.PackageClassTest", packageClass.getCanonicalName());
    }

    @Test
    public void testInnerClassInput() {
        PackageClass packageClass = new PackageClass(Inner$Stuff.class);

        assertEquals("org.androidtransfuse.model.PackageClassTest$Inner$Stuff", packageClass.getFullyQualifiedName());
        assertEquals("PackageClassTest$Inner$Stuff", packageClass.getClassName());
        assertEquals("org.androidtransfuse.model", packageClass.getPackage());
        assertEquals("org.androidtransfuse.model.PackageClassTest.Inner$Stuff", packageClass.getCanonicalName());
    }

    @Test
    public void testClassNameParsing() {
        PackageClass packageClass = new PackageClass(FULLY_QUALIFIED_NAME);

        assertEquals(FULLY_QUALIFIED_NAME, packageClass.getCanonicalName());
    }

    @Test
    public void testSeparateClassPackage() {
        PackageClass packageClass = new PackageClass(PACKAGE_NAME, CLASS_NAME);

        assertEquals(FULLY_QUALIFIED_NAME, packageClass.getCanonicalName());
    }

    @Test
    public void testDotJavaRemoval() {
        PackageClass packageClass = new PackageClass(FULLY_QUALIFIED_NAME + DOT_JAVA);

        assertEquals(FULLY_QUALIFIED_NAME, packageClass.getCanonicalName());
    }

    @Test
    public void testAppend() {
        PackageClass packageClass = new PackageClass(FULLY_QUALIFIED_NAME).append(TEST_NAME);

        assertEquals(FULLY_QUALIFIED_NAME + TEST_NAME, packageClass.getCanonicalName());
    }

    @Test
    public void testNoPackage() {
        PackageClass packageClass = new PackageClass(CLASS_NAME);

        assertEquals(CLASS_NAME, packageClass.getCanonicalName());
    }

    @Test
    public void testReplace() {
        PackageClass packageClass = new PackageClass(FULLY_QUALIFIED_NAME).replaceName(TEST_NAME);

        assertEquals(PACKAGE_NAME + "." + TEST_NAME, packageClass.getCanonicalName());
    }

    @Test
    public void testEquals() {
        PackageClass one = new PackageClass(FULLY_QUALIFIED_NAME);
        PackageClass two = new PackageClass(FULLY_QUALIFIED_NAME);
        PackageClass three = one.append(TEST_NAME);

        assertEquals(one, two);
        assertFalse(one.equals(three));
        assertEquals(one.hashCode(), two.hashCode());
        assertNotSame(one.hashCode(), three.hashCode());
    }
}
