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

    public <T> T merge(T target, T source) throws IllegalAccessException, PrivilegedActionException {

        if (target == null) {
            return source;
        } else if (source == null) {
            return null;
        }

        Class targetClass = target.getClass();

        if (!Mergable.class.isAssignableFrom(targetClass)) {
            return source;
        }

        for (Field field : targetClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(MergeCollection.class) && Collection.class.isAssignableFrom(field.getType())) {
                updateField(field, target, mergeCollection((Collection) getField(field, target), (Collection) getField(field, source)));
            }
            if (field.isAnnotationPresent(Merge.class)) {
                updateField(field, target, merge(getField(field, target), getField(field, source)));
            }
        }

        return target;
    }

    private Object getField(Field field, final Object source) throws PrivilegedActionException {
        return AccessController.doPrivileged(new AccessibleElementPrivilegedAction<Object, Field>(field) {

            @Override
            public Object run(Field field) throws Exception {
                return field.get(source);
            }
        });
    }

    private <T> void updateField(Field field, final T target, final Object value) throws PrivilegedActionException {
        AccessController.doPrivileged(new AccessibleElementPrivilegedAction<Void, Field>(field) {

            @Override
            public Void run(Field field) throws Exception {
                field.set(target, value);
                return null;
            }
        });
    }

    private Collection mergeCollection(Collection target, Collection source) throws IllegalAccessException, PrivilegedActionException {

        Map<Object, Mergable> targetMap = convertToMergable(target);
        Map<Object, Mergable> sourceMap = convertToMergable(source);
        Set<Object> originalTargetKeys = new HashSet<Object>(targetMap.keySet());

        for (Map.Entry<Object, Mergable> mergableSourceEntry : sourceMap.entrySet()) {

            Object sourceKey = mergableSourceEntry.getKey();

            if (targetMap.containsKey(sourceKey)) {
                //replace
                Mergable targetValue = targetMap.get(sourceKey);
                if (targetValue.getMergeTag() != null) {
                    targetMap.put(sourceKey, merge(targetValue, mergableSourceEntry.getValue()));
                }
            } else {
                targetMap.put(sourceKey, mergableSourceEntry.getValue());
            }
            originalTargetKeys.remove(sourceKey);
        }

        for (Object targetKey : originalTargetKeys) {
            Mergable mergable = targetMap.get(targetKey);

            if (mergable.getMergeTag() != null) {
                targetMap.remove(targetKey);
            }
        }

        target.clear();
        target.addAll(targetMap.values());
        return target;
    }

    private Map<Object, Mergable> convertToMergable(Collection input) {
        Map<Object, Mergable> mergeable = new HashMap<Object, Mergable>();

        if (input != null) {
            for (Object o : input) {
                //validate all instance are of type Mergeable
                if (!(o instanceof Mergable)) {
                    throw new RoboticsAnalysisException("Merge collection failed on collection");
                }
                Mergable t = (Mergable) o;
                mergeable.put(t.getIdentifier(), t);

            }
        }

        return mergeable;
    }
}
