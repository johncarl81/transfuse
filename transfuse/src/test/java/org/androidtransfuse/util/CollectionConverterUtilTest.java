package org.androidtransfuse.util;

import org.androidtransfuse.TransfuseTestInjector;
import org.apache.commons.collections.ListUtils;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class CollectionConverterUtilTest {

    @Inject
    private CollectionConverterUtil collectionConverterUtil;
    @Inject
    private IntToStringConverter converter;

    private static final class IntToStringConverter implements CollectionConverter<Integer, String> {

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
        List<String> output = collectionConverterUtil.transform(input, converter);

        assertTrue(ListUtils.isEqualList(Arrays.asList("1", "2", "3", "4"), output));
    }


}
