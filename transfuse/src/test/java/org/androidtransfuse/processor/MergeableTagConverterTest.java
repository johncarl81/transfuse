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
package org.androidtransfuse.processor;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

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
    public void testToString() throws Exception {
        MergeableTags mergeable = new MergeableTags(TAGS);

        String converted = converter.marshal(mergeable);

        assertEquals(CONVERTED_TAGS, converted);
    }

    @Test
    public void testEmptyInput() throws Exception {
        MergeableTags mergeable = new MergeableTags();

        assertNull(converter.marshal(mergeable));
    }
    @Test
    public void testNullInput() throws Exception {
        assertNull(converter.marshal(null));
    }

    @Test
    public void testFromEmptyString() throws Exception {
        Object output = converter.unmarshal("");

        assertNotNull(output);

        MergeableTags tags = (MergeableTags)output;

        assertTrue(tags.getTags().isEmpty());
    }

    @Test
    public void testFromNullString() throws Exception {
        Object output = converter.unmarshal(null);

        assertNotNull(output);

        MergeableTags tags = (MergeableTags)output;

        assertTrue(tags.getTags().isEmpty());
    }

    @Test
    public void testFromString() throws Exception {
        Object output = converter.unmarshal(CONVERTED_TAGS);

        assertNotNull(output);

        MergeableTags tags = (MergeableTags)output;

        assertEquals(TAGS.size(), tags.getTags().size());
        assertTrue(tags.getTags().containsAll(TAGS));
    }
}
