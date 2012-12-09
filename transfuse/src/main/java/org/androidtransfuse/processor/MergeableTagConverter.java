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
