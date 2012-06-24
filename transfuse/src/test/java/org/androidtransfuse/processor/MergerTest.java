package org.androidtransfuse.processor;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.model.Identified;
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

    public static class MergeableRoot extends Mergeable implements Identified {
        String id;
        String dontMerge;
        String stringValue;
        int intValue;
        List<SubMergeable> subMergeables = new ArrayList<SubMergeable>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDontMerge() {
            return dontMerge;
        }

        public void setDontMerge(String dontMerge) {
            this.dontMerge = dontMerge;
        }

        @Merge("v")
        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        @Merge("i")
        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        @MergeCollection(collectionType = ArrayList.class, type = SubMergeable.class)
        public List<SubMergeable> getSubMergeables() {
            return subMergeables;
        }

        public void setSubMergeables(List<SubMergeable> subMergeables) {
            this.subMergeables = subMergeables;
        }

        @Override
        public String getIdentifier() {
            return id;
        }
    }

    public static class SubMergeable extends Mergeable implements Identified {
        String value;
        String dontMergeValue;
        String id;

        @Merge("v")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDontMergeValue() {
            return dontMergeValue;
        }

        public void setDontMergeValue(String dontMergeValue) {
            this.dontMergeValue = dontMergeValue;
        }

        @Merge("i")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String getIdentifier() {
            return id;
        }
    }

    public static class OneContainer extends Mergeable{
        private List<One> ones = new ArrayList<One>();

        @MergeCollection(collectionType = ArrayList.class, type = One.class)
        public List<One> getOnes() {
            return ones;
        }

        public void setOnes(List<One> ones) {
            this.ones = ones;
        }
    }

    public static class One extends Mergeable implements Identified{

        private String name;
        private List<Two> two = new ArrayList<Two>();

        @Merge("n")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @MergeCollection(collectionType = ArrayList.class, type = Two.class)
        public List<Two> getTwo() {
            return two;
        }

        public void setTwo(List<Two> two) {
            this.two = two;
        }

        @Override
        public String getIdentifier() {
            return name;
        }
    }

    //non identified, should merge on order
    public static class Two extends Mergeable{
        private List<Three> three = new ArrayList<Three>();

        @MergeCollection(collectionType = ArrayList.class, type = Three.class)
        public List<Three> getThree() {
            return three;
        }

        public void setThree(List<Three> three) {
            this.three = three;
        }
    }

    public static class Three extends Mergeable implements Identified{
        private String name;
        private String value;

        @Merge("n")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Merge("v")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String getIdentifier() {
            return name;
        }
    }

    @Inject
    private Provider<SubMergeable> subMergeableProvider;
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
        List<SubMergeable> subMergeablesOne = new ArrayList<SubMergeable>();
        List<SubMergeable> subMergeablesTwo = new ArrayList<SubMergeable>();

        subMergeablesOne.add(buildSubMergeable("1", "five", "six", true));
        subMergeablesTwo.add(buildSubMergeable("1", "seven", "eight", true));

        MergeableRoot one = buildMergeableRoot("2", "one", "two", 5, subMergeablesOne, true);
        MergeableRoot two = buildMergeableRoot("2", "three", "four", 6, subMergeablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertEquals("four", merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(1, merged.getSubMergeables().size());
        SubMergeable subMergeable = merged.getSubMergeables().iterator().next();
        assertEquals("seven", subMergeable.getValue());
        assertEquals("six", subMergeable.getDontMergeValue());
    }

    @Test
    public void testNonMatchingMerge() throws MergerException {
        List<SubMergeable> subMergeablesOne = new ArrayList<SubMergeable>();
        List<SubMergeable> subMergeablesTwo = new ArrayList<SubMergeable>();

        subMergeablesOne.add(buildSubMergeable("1", "five", "six", true));
        subMergeablesTwo.add(buildSubMergeable("2", "seven", "eight", true));

        MergeableRoot one = buildMergeableRoot("3", "one", "two", 5, subMergeablesOne, true);
        MergeableRoot two = buildMergeableRoot("4", "three", "four", 6, subMergeablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertEquals("four", merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(1, merged.getSubMergeables().size());
        SubMergeable subMergeableTwo = buildSubMergeableMap(merged.getSubMergeables()).get("2");
        assertEquals("seven", subMergeableTwo.getValue());
        assertEquals("eight", subMergeableTwo.getDontMergeValue());
    }

    @Test
    public void testNullMerge() throws MergerException {
        List<SubMergeable> subMergeablesOne = new ArrayList<SubMergeable>();
        List<SubMergeable> subMergeablesTwo = new ArrayList<SubMergeable>();

        subMergeablesOne.add(buildSubMergeable("1", "seven", "eight", true));
        subMergeablesTwo.add(buildSubMergeable("1", null, null, true));

        MergeableRoot one = buildMergeableRoot("3", "one", "two", 5, subMergeablesOne, true);
        MergeableRoot two = buildMergeableRoot("3", null, null, 6, subMergeablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertNull(merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(1, merged.getSubMergeables().size());
        SubMergeable subMergeable = merged.getSubMergeables().iterator().next();
        assertNull(subMergeable.getValue());
        assertEquals(2, subMergeable.getMergeTagSize());
        assertEquals("eight", subMergeable.getDontMergeValue());
    }

    @Test
    public void testNullCollection() throws MergerException {
        List<SubMergeable> subMergeablesTwo = new ArrayList<SubMergeable>();

        subMergeablesTwo.add(buildSubMergeable("1", "seven", "eight", true));

        MergeableRoot one = buildMergeableRoot("3", "one", "two", 5, null, true);
        MergeableRoot two = buildMergeableRoot("3", null, null, 6, subMergeablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertNull(merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(1, merged.getSubMergeables().size());
        SubMergeable subMergeable = merged.getSubMergeables().iterator().next();
        assertEquals("seven", subMergeable.getValue());
        assertEquals("eight", subMergeable.getDontMergeValue());
    }

    @Test
    public void testPreviouslyUntagWritten() throws MergerException {
        List<SubMergeable> subMergeablesOne = new ArrayList<SubMergeable>();
        List<SubMergeable> subMergeablesTwo = new ArrayList<SubMergeable>();

        subMergeablesOne.add(buildSubMergeable("1", "five", "six", false));
        subMergeablesOne.add(buildSubMergeable("2", "nine", "ten", false));
        subMergeablesTwo.add(buildSubMergeable("1", "seven", "eight", true));

        MergeableRoot one = buildMergeableRoot("2", "one", "two", 5, subMergeablesOne, false);
        MergeableRoot two = buildMergeableRoot("2", "three", "four", 6, subMergeablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertEquals("two", merged.getStringValue());
        assertEquals(5, merged.getIntValue());
        assertEquals(2, merged.getSubMergeables().size());
        SubMergeable subMergeable = buildSubMergeableMap(merged.getSubMergeables()).get("1");
        assertEquals("five", subMergeable.getValue());
        assertEquals("six", subMergeable.getDontMergeValue());
    }

    @Test
    public void testEmptyCollection() throws MergerException {
        List<SubMergeable> subMergeablesOne = new ArrayList<SubMergeable>();
        List<SubMergeable> subMergeablesTwo = new ArrayList<SubMergeable>();

        subMergeablesOne.add(buildSubMergeable("1", "five", "six", true));

        MergeableRoot one = buildMergeableRoot("2", "one", "two", 5, subMergeablesOne, true);
        MergeableRoot two = buildMergeableRoot("2", "three", "four", 6, subMergeablesTwo, true);

        MergeableRoot merged = merger.merge(MergeableRoot.class, one, two);

        assertEquals("one", merged.getDontMerge());
        assertEquals("four", merged.getStringValue());
        assertEquals(6, merged.getIntValue());
        assertEquals(0, merged.getSubMergeables().size());
    }

    @Test
    public void testUnidentified() throws MergerException {
        One before = buildOne("1", buildTwo(buildThree("3", "before")));
        One after = buildOne("1", buildTwo(buildThree("3", "after")));

        One merged = merger.merge(One.class, before, after);

        assertEquals(1, merged.getTwo().size());
        Two mergedTwo = merged.getTwo().get(0);
        assertEquals(1, mergedTwo.getThree().size());
        Three mergedThree = mergedTwo.getThree().get(0);
        assertEquals("after", mergedThree.getValue());
    }

    @Test
    public void testUnidentifiedList() throws MergerException {
        OneContainer before = new OneContainer();
        OneContainer after = new OneContainer();

        before.getOnes().add(buildOne("1", buildTwo(buildThree("3", "before"))));
        before.getOnes().add(buildOne("2"));
        after.getOnes().add(buildOne("1"));
        after.getOnes().add(buildOne("2", buildTwo(buildThree("3", "after"))));

        OneContainer merged = merger.merge(OneContainer.class, before, after);

        assertEquals(2, merged.getOnes().size());
        One first = merged.getOnes().get(0);
        One second = merged.getOnes().get(1);
        //assertEquals(0, first.getTwo().size());
        assertEquals(1, second.getTwo().size());
        Two mergedTwo = second.getTwo().get(0);
        assertEquals(1, mergedTwo.getThree().size());
        Three mergedThree = mergedTwo.getThree().get(0);
        assertEquals("after", mergedThree.getValue());
    }

    private One buildOne(String name, Two... twos) throws MergerException {
        One one = new One();
        one.setName(name);
        if(twos != null){
            for (Two two : twos) {
                one.getTwo().add(two);
            }
        }
        return updateMergeTags(one);
    }

    private Two buildTwo(Three... threes) throws MergerException {
        Two two = new Two();
        if(threes != null){
            for (Three three : threes) {
                two.getThree().add(three);
            }
        }
        return updateMergeTags(two);
    }

    private Three buildThree(String name, String value) throws MergerException {
        Three three = new Three();
        three.setName(name);
        three.setValue(value);
        return updateMergeTags(three);
    }


    private Map<String, SubMergeable> buildSubMergeableMap(List<SubMergeable> subMergeables) {

        Map<String, SubMergeable> subMergeableMap = new HashMap<String, SubMergeable>();
        for (SubMergeable subMergeable : subMergeables) {
            subMergeableMap.put(subMergeable.getIdentifier(), subMergeable);
        }

        return subMergeableMap;
    }


    private MergeableRoot buildMergeableRoot(String id, String dontMerge, String stringValue, int intValue, List<SubMergeable> subMergeables, boolean generateTag) throws MergerException {

        MergeableRoot mergeableRoot = mergeableRootProvider.get();

        mergeableRoot.setId(id);
        mergeableRoot.setDontMerge(dontMerge);
        mergeableRoot.setStringValue(stringValue);
        mergeableRoot.setIntValue(intValue);
        mergeableRoot.setSubMergeables(subMergeables);

        if (generateTag) {
            updateMergeTags(mergeableRoot);
        }


        return mergeableRoot;
    }

    private SubMergeable buildSubMergeable(String id, String value, String dontMergeValue, boolean generateTag) throws MergerException {

        SubMergeable subMergeable = subMergeableProvider.get();

        subMergeable.setId(id);
        subMergeable.setValue(value);
        subMergeable.setDontMergeValue(dontMergeValue);

        if (generateTag) {
            updateMergeTags(subMergeable);
        }

        return subMergeable;
    }

    private <T extends Mergeable> T updateMergeTags(T mergeable) throws MergerException {
        try {
            mergeable.setGenerated(true);

            BeanInfo beanInfo = Introspector.getBeanInfo(mergeable.getClass());

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

        return mergeable;
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
