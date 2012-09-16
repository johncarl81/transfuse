package org.androidtransfuse.util;

import org.androidtransfuse.TransfuseTestInjector;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.androidtransfuse.util.CollectionConverterUtil.transform;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class CollectionConverterUtilTest {

    @Inject
    private IntToStringConverter converter;

    private static final class IntToStringConverter implements Conversion<Integer, String> {

        @Override
        public String convert(Integer integer) {
            if (integer != null) {
                return integer.toString();
            }
            return null;
        }
    }

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);
    }

    @Test
    public void testConversion() {
        List<Integer> input = Arrays.asList(1, 2, 3, null, 4);
        Collection<String> output = transform(input, converter);

        assertTrue(CollectionUtils.isEqualCollection(Arrays.asList("1", "2", "3", "4"), output));
    }


}
