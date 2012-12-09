package org.androidtransfuse.processor;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
public class MergeableTagConverterTest {

    private static final Set<String> TAGS = new TreeSet<String>(Arrays.asList("a", "b", "c"));
    private static final String CONVERTED_TAGS = "a,b,c";
    private MergeableTagConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new MergeableTagConverter();
    }

    @Test
    public void testCanConvert(){
        assertTrue(converter.canConvert(MergeableTags.class));
        assertFalse(converter.canConvert(Object.class));
    }

    @Test
    public void testToString(){
        MergeableTags mergable = new MergeableTags(TAGS);

        String converted = converter.toString(mergable);

        assertEquals(CONVERTED_TAGS, converted);
    }

    @Test
    public void testEmptyInput(){
        MergeableTags mergable = new MergeableTags();

        assertNull(converter.toString(mergable));
    }
    @Test
    public void testNullInput(){
        assertNull(converter.toString(null));
    }

    @Test
    public void testFromEmptyString(){
        Object output = converter.fromString("");

        assertTrue(output instanceof MergeableTags);

        MergeableTags tags = (MergeableTags)output;

        assertTrue(tags.getTags().isEmpty());
    }

    @Test
    public void testFromNullString(){
        Object output = converter.fromString(null);

        assertTrue(output instanceof MergeableTags);

        MergeableTags tags = (MergeableTags)output;

        assertTrue(tags.getTags().isEmpty());
    }

    @Test
    public void testFromString(){
        Object output = converter.fromString(CONVERTED_TAGS);

        assertTrue(output instanceof MergeableTags);

        MergeableTags tags = (MergeableTags)output;

        assertEquals(TAGS.size(), tags.getTags().size());
        assertTrue(tags.getTags().containsAll(TAGS));
    }
}
