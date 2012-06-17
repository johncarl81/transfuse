package org.androidtransfuse.processor;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.model.Mergeable;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author John Ericksen
 */
public class MergerTest {

    public static class MergeableRoot extends Mergeable {
        String id;
        String dontMerge;
        String stringValue;
        int intValue;
        List<SubMergable> subMergables = new ArrayList<SubMergable>();

        public String getDontMerge() {
            return dontMerge;
        }

        public String getStringValue() {
            return stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        @MergeCollection(collectionType = ArrayList.class, type = SubMergable.class)
        public List<SubMergable> getSubMergables() {
            return subMergables;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setDontMerge(String dontMerge) {
            this.dontMerge = dontMerge;
        }

        @Merge("v")
        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        @Merge("i")
        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public void setSubMergables(List<SubMergable> subMergables) {
            this.subMergables = subMergables;
        }

        @Override
        public String getIdentifier() {
            return id;
        }

        public String getId() {
            return id;
        }
    }

    public static class SubMergable extends Mergeable {
        String value;
        String dontMergeValue;
        String id;

        public String getValue() {
            return value;
        }

        public String getDontMergeValue() {
            return dontMergeValue;
        }

        @Override
        public String getIdentifier() {
            return id;
        }

        @Merge("v")
        public void setValue(String value) {
            this.value = value;
        }

        public void setDontMergeValue(String dontMergeValue) {
            this.dontMergeValue = dontMergeValue;
        }

        @Merge("i")
        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    @Inject
    private Provider<SubMergable> subMergableProvider;
    @Inject
    private Provider<MergeableRoot> mergeableRootProvider;
    @Inject
    private Merger merger;

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);
    }

    @Test
    public void testMerge() throws MergerException {
        List<SubMergable> subMergablesOne = new ArrayList<SubMergable>();
        List<SubMergable> subMergablesTwo = new ArrayList<SubMergable>();

        subMergablesOne.add(buildSubMergable("1", "five", "six", true));
        subMergablesTwo.add(buildSubMergable("1", "seven", "eight", true));

        MergeableRoot one = buildMergeableRoot("2", "one", "two", 5, subMergablesOne, true);
        MergeableRoot two = buildMergeableRoot("2", "three", "four", 6, subMergablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertEquals("four", merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(1, merged.getSubMergables().size());
        SubMergable subMergable = merged.getSubMergables().iterator().next();
        assertEquals("seven", subMergable.getValue());
        assertEquals("six", subMergable.getDontMergeValue());
    }

    @Test
    public void testNonMatchingMerge() throws MergerException {
        List<SubMergable> subMergablesOne = new ArrayList<SubMergable>();
        List<SubMergable> subMergablesTwo = new ArrayList<SubMergable>();

        subMergablesOne.add(buildSubMergable("1", "five", "six", true));
        subMergablesTwo.add(buildSubMergable("2", "seven", "eight", true));

        MergeableRoot one = buildMergeableRoot("3", "one", "two", 5, subMergablesOne, true);
        MergeableRoot two = buildMergeableRoot("4", "three", "four", 6, subMergablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertEquals("four", merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(1, merged.getSubMergables().size());
        SubMergable subMergableTwo = buildSubMergableMap(merged.getSubMergables()).get("2");
        assertEquals("seven", subMergableTwo.getValue());
        assertEquals("eight", subMergableTwo.getDontMergeValue());
    }

    @Test
    public void testNullMerge() throws MergerException {
        List<SubMergable> subMergablesOne = new ArrayList<SubMergable>();
        List<SubMergable> subMergablesTwo = new ArrayList<SubMergable>();

        subMergablesOne.add(buildSubMergable("1", "seven", "eight", true));
        subMergablesTwo.add(buildSubMergable("1", null, null, true));

        MergeableRoot one = buildMergeableRoot("3", "one", "two", 5, subMergablesOne, true);
        MergeableRoot two = buildMergeableRoot("3", null, null, 6, subMergablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertNull(merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(1, merged.getSubMergables().size());
        SubMergable subMergable = merged.getSubMergables().iterator().next();
        assertNull(subMergable.getValue());
        assertEquals(1, subMergable.getMergeTags().size());
        assertEquals("eight", subMergable.getDontMergeValue());
    }

    @Test
    public void testNullCollection() throws MergerException {
        List<SubMergable> subMergablesTwo = new ArrayList<SubMergable>();

        subMergablesTwo.add(buildSubMergable("1", "seven", "eight", true));

        MergeableRoot one = buildMergeableRoot("3", "one", "two", 5, null, true);
        MergeableRoot two = buildMergeableRoot("3", null, null, 6, subMergablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertNull(merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(1, merged.getSubMergables().size());
        SubMergable subMergable = merged.getSubMergables().iterator().next();
        assertEquals("seven", subMergable.getValue());
        assertEquals("eight", subMergable.getDontMergeValue());
    }

    @Test
    public void testPreviouslyUntagWritten() throws MergerException {
        List<SubMergable> subMergablesOne = new ArrayList<SubMergable>();
        List<SubMergable> subMergablesTwo = new ArrayList<SubMergable>();

        subMergablesOne.add(buildSubMergable("1", "five", "six", false));
        subMergablesOne.add(buildSubMergable("2", "nine", "ten", false));
        subMergablesTwo.add(buildSubMergable("1", "seven", "eight", true));

        MergeableRoot one = buildMergeableRoot("2", "one", "two", 5, subMergablesOne, false);
        MergeableRoot two = buildMergeableRoot("2", "three", "four", 6, subMergablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertEquals("two", merged.getStringValue());
        assertEquals(5, merged.getIntValue());
        assertEquals(2, merged.getSubMergables().size());
        SubMergable subMergable = buildSubMergableMap(merged.getSubMergables()).get("1");
        assertEquals("five", subMergable.getValue());
        assertEquals("six", subMergable.getDontMergeValue());
    }

    @Test
    public void testEmptyCollection() throws MergerException {
        List<SubMergable> subMergablesOne = new ArrayList<SubMergable>();
        List<SubMergable> subMergablesTwo = new ArrayList<SubMergable>();

        subMergablesOne.add(buildSubMergable("1", "five", "six", true));

        MergeableRoot one = buildMergeableRoot("2", "one", "two", 5, subMergablesOne, true);
        MergeableRoot two = buildMergeableRoot("2", "three", "four", 6, subMergablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertEquals("four", merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(0, merged.getSubMergables().size());
    }

    private Map<String, SubMergable> buildSubMergableMap(List<SubMergable> subMergables) {

        Map<String, SubMergable> subMergableMap = new HashMap<String, SubMergable>();
        for (SubMergable subMergable : subMergables) {
            subMergableMap.put(subMergable.getIdentifier(), subMergable);
        }

        return subMergableMap;
    }


    private MergeableRoot buildMergeableRoot(String id, String dontMerge, String stringValue, int intValue, List<SubMergable> subMergables, boolean generateTag) throws MergerException {

        MergeableRoot mergeableRoot = mergeableRootProvider.get();

        mergeableRoot.setId(id);
        mergeableRoot.setDontMerge(dontMerge);
        mergeableRoot.setStringValue(stringValue);
        mergeableRoot.setIntValue(intValue);
        mergeableRoot.setSubMergables(subMergables);

        if (generateTag) {
            updateMergeTags(MergeableRoot.class, mergeableRoot);
        }


        return mergeableRoot;
    }

    private SubMergable buildSubMergable(String id, String value, String dontMergeValue, boolean generateTag) throws MergerException {

        SubMergable subMergable = subMergableProvider.get();

        subMergable.setId(id);
        subMergable.setValue(value);
        subMergable.setDontMergeValue(dontMergeValue);

        if (generateTag) {
            updateMergeTags(SubMergable.class, subMergable);
        }

        return subMergable;
    }

    private <T extends Mergeable> void updateMergeTags(Class<T> clazz, T mergeable) throws MergerException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);

            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                Method readMethod = propertyDescriptor.getReadMethod();
                Method writeMethod = propertyDescriptor.getWriteMethod();

                Merge mergeAnnotation = findAnnotation(Merge.class, writeMethod, readMethod);

                if (mergeAnnotation != null) {
                    mergeable.addMergeTag(mergeAnnotation.value());
                }
            }
        } catch (IntrospectionException e) {
            throw new MergerException(e);
        }
    }

    private <T extends Annotation> T findAnnotation(Class<T> annotationClass, Method... methods) {
        T annotation = null;
        if (methods != null) {
            for (Method method : methods) {
                if (annotation == null && method != null && method.isAnnotationPresent(annotationClass)) {
                    annotation = method.getAnnotation(annotationClass);
                }
            }
        }
        return annotation;
    }

}
