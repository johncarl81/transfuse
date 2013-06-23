package org.androidtransfuse.scope;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
public class ScopeKeyTest {

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method getMethod = ScopeKey.class.getMethod(ScopeKey.GET_METHOD, Class.class, String.class);
        assertNotNull(getMethod);
    }

    @Test
    public void testClassEqual(){
        ScopeKey one = ScopeKey.of(ScopeKeyTest.class);
        ScopeKey two = ScopeKey.of(ScopeKeyTest.class);

        assertEquals(one, two);
    }

    @Test
    public void testClassNotEqual(){
        ScopeKey one = ScopeKey.of(ScopeKeyTest.class);
        ScopeKey two = ScopeKey.of(Method.class);

        assertNotEquals(one, two);
    }

    @Test
    public void testAnnotationEqual(){
        String annotation = "@Test";
        ScopeKey one = ScopeKey.of(ScopeKeyTest.class).annotatedBy(annotation);
        ScopeKey two = ScopeKey.of(ScopeKeyTest.class).annotatedBy(annotation);

        assertEquals(one, two);
    }

    @Test
    public void testAnnotationNotEqual(){
        ScopeKey one = ScopeKey.of(ScopeKeyTest.class).annotatedBy("@One");
        ScopeKey two = ScopeKey.of(ScopeKeyTest.class).annotatedBy("@Two");

        assertNotEquals(one, two);
    }
}
