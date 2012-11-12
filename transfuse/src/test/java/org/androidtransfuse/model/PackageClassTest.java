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

    public static class Inner {
    }

    @Test
    public void testClassInput() {
        PackageClass packageClass = new PackageClass(PackageClassTest.class);

        assertEquals(PackageClassTest.class.getName(), packageClass.getFullyQualifiedName());
    }

    @Test
    public void testInnerClassInput() {
        PackageClass packageClass = new PackageClass(Inner.class);

        assertEquals(Inner.class.getName(), packageClass.getFullyQualifiedName());
    }

    @Test
    public void testClassNameParsing() {
        PackageClass packageClass = new PackageClass(FULLY_QUALIFIED_NAME);

        assertEquals(FULLY_QUALIFIED_NAME, packageClass.getFullyQualifiedName());
    }

    @Test
    public void testSeparateClassPackage() {
        PackageClass packageClass = new PackageClass(PACKAGE_NAME, CLASS_NAME);

        assertEquals(FULLY_QUALIFIED_NAME, packageClass.getFullyQualifiedName());
    }

    @Test
    public void testDotJavaRemoval() {
        PackageClass packageClass = new PackageClass(FULLY_QUALIFIED_NAME + DOT_JAVA);

        assertEquals(FULLY_QUALIFIED_NAME, packageClass.getFullyQualifiedName());
    }

    @Test
    public void testAppend() {
        PackageClass packageClass = new PackageClass(FULLY_QUALIFIED_NAME).append(TEST_NAME);

        assertEquals(FULLY_QUALIFIED_NAME + TEST_NAME, packageClass.getFullyQualifiedName());
    }

    @Test
    public void testNoPackage() {
        PackageClass packageClass = new PackageClass(CLASS_NAME);

        assertEquals(CLASS_NAME, packageClass.getFullyQualifiedName());
    }

    @Test
    public void testReplace() {
        PackageClass packageClass = new PackageClass(FULLY_QUALIFIED_NAME).replaceName(TEST_NAME);

        assertEquals(PACKAGE_NAME + "." + TEST_NAME, packageClass.getFullyQualifiedName());
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
