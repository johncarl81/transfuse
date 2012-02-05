package org.androidrobotics.processor;

import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.util.AccessibleElementPrivilegedAction;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.*;

/**
 * @author John Ericksen
 */
public class Merger {

    public <T> T merge(T target, T source) throws IllegalAccessException, PrivilegedActionException, InstantiationException {

        if (target == null) {
            return source;
        } else if (source == null) {
            return null;
        }

        Class targetClass = target.getClass();

        if (!Mergeable.class.isAssignableFrom(targetClass)) {
            return source;
        }

        for (Field field : targetClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(MergeCollection.class) && Collection.class.isAssignableFrom(field.getType())) {
                updateField(field, target, mergeCollection(field, (Collection) getField(field, target), (Collection) getField(field, source)));
            }
            if (field.isAnnotationPresent(Merge.class)) {
                updateField(field, target, merge(getField(field, target), getField(field, source)));
            }
        }

        return target;
    }

    private Object getField(Field field, final Object source) throws PrivilegedActionException {
        return AccessController.doPrivileged(new FieldGetPriviledgedAction(field, source));
    }

    private static class FieldGetPriviledgedAction extends AccessibleElementPrivilegedAction<Object, Field> {

        private Object source;

        protected FieldGetPriviledgedAction(Field field, Object source) {
            super(field);
            this.source = source;
        }

        @Override
        public Object run(Field field) throws Exception {
            return field.get(source);
        }
    }

    private <T> void updateField(Field field, final T target, final Object value) throws PrivilegedActionException {
        AccessController.doPrivileged(new FieldSetPriviledgedAction(field, target, value));
    }

    private static class FieldSetPriviledgedAction extends AccessibleElementPrivilegedAction<Void, Field> {

        private Object target;
        private Object value;

        protected FieldSetPriviledgedAction(Field field, Object target, Object value) {
            super(field);
            this.target = target;
            this.value = value;
        }

        @Override
        public Void run(Field field) throws Exception {
            field.set(target, value);
            return null;
        }
    }

    private Collection mergeCollection(Field field, Collection target, Collection source) throws IllegalAccessException, PrivilegedActionException, InstantiationException {

        Map<Object, Mergeable> targetMap = convertToMergable(target);
        Map<Object, Mergeable> sourceMap = convertToMergable(source);
        Set<Object> originalTargetKeys = new HashSet<Object>(targetMap.keySet());

        for (Map.Entry<Object, Mergeable> mergableSourceEntry : sourceMap.entrySet()) {

            Object sourceKey = mergableSourceEntry.getKey();

            if (targetMap.containsKey(sourceKey)) {
                //replace
                Mergeable targetValue = targetMap.get(sourceKey);
                if (targetValue.getMergeTag() != null) {
                    targetMap.put(sourceKey, merge(targetValue, mergableSourceEntry.getValue()));
                }
            } else {
                targetMap.put(sourceKey, mergableSourceEntry.getValue());
            }
            originalTargetKeys.remove(sourceKey);
        }

        for (Object targetKey : originalTargetKeys) {
            Mergeable mergable = targetMap.get(targetKey);

            if (mergable.getMergeTag() != null) {
                targetMap.remove(targetKey);
            }
        }

        Collection targetResult = target;
        //merger only supports Lists for collections
        if (targetResult == null) {
            //first look for specific impl in annotation
            MergeCollection mergeCollectionAnnotation = field.getAnnotation(MergeCollection.class);
            if (mergeCollectionAnnotation.targetType() != Collection.class) {
                targetResult = mergeCollectionAnnotation.targetType().newInstance();
            } else {
                //try to instantiate field type
                targetResult = (Collection) field.getType().newInstance();
            }
        }

        targetResult.clear();
        targetResult.addAll(targetMap.values());
        return targetResult;
    }

    private Map<Object, Mergeable> convertToMergable(Collection input) {
        Map<Object, Mergeable> mergeable = new HashMap<Object, Mergeable>();

        if (input != null) {
            for (Object o : input) {
                //validate all instance are of type Mergeable
                if (!(o instanceof Mergeable)) {
                    throw new RoboticsAnalysisException("Merge collection failed on collection");
                }
                Mergeable t = (Mergeable) o;
                mergeable.put(t.getIdentifier(), t);

            }
        }

        return mergeable;
    }
}
