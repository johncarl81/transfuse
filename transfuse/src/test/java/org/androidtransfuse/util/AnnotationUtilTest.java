package org.androidtransfuse.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author John Ericksen
 */
public class AnnotationUtilTest {

    @Test
    public void testCheckDefault(){
        assertEquals("test", AnnotationUtil.checkDefault("test", "default"));
        assertNull(AnnotationUtil.checkDefault("default", "default"));
    }

    @Test
    public void test(){
        assertNull(AnnotationUtil.checkBlank(null));
        assertNull(AnnotationUtil.checkBlank(""));
        assertNull(AnnotationUtil.checkBlank(" "));
        assertEquals("test", AnnotationUtil.checkBlank("test"));
    }
}
