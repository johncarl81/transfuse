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
