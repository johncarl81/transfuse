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
package org.androidtransfuse.processor;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class MergeableTagConverter extends AbstractSingleValueConverter {

    private static final String SPLIT = ",";

    @Override
    public boolean canConvert(Class type) {
        return MergeableTags.class.isAssignableFrom(type);
    }

    @Override
    public String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        Set<String> stringCollection = ((MergeableTags) obj).getTags();

        if (stringCollection.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        boolean first = true;

        for (String s : stringCollection) {
            if (first) {
                first = false;
            } else {
                builder.append(SPLIT);
            }
            builder.append(s);
        }

        return builder.toString();
    }

    @Override
    public Object fromString(String input) {

        Set<String> tagValues = new HashSet<String>();

        if(input != null && !input.isEmpty()){
            String[] splitInput = input.split(SPLIT);
            tagValues.addAll(Arrays.asList(splitInput));
        }

        return new MergeableTags(tagValues);
    }
}
