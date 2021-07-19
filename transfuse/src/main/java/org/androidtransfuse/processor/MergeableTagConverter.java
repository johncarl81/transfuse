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

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class MergeableTagConverter extends XmlAdapter<String, MergeableTags> {

    private static final String SPLIT = ",";

    @Override
    public String marshal(MergeableTags mergeableTags) throws Exception {
        if (mergeableTags == null) {
            return null;
        }
        if (mergeableTags.getTags().isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        boolean first = true;

        for (String s : mergeableTags.getTags()) {
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
    public MergeableTags unmarshal(String input) throws Exception {
        Set<String> tagValues = new HashSet<String>();

        if(input != null && !input.isEmpty()){
            String[] splitInput = input.split(SPLIT);
            tagValues.addAll(Arrays.asList(splitInput));
        }

        return new MergeableTags(tagValues);
    }
}
