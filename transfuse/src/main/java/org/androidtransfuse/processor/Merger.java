package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author John Ericksen
 */
public class Merger {

    public <T> T merge(Class<? extends T> targetClass, T target, T source) throws MergerException {

        if (target == null) {
            return source;
        } else if (source == null) {
            return target;
        }

        if (!Mergeable.class.isAssignableFrom(targetClass)) {
            return target;
        }

        return (T) mergeMergeable((Class<? extends Mergeable>) targetClass, (Mergeable) target, (Mergeable) source);
    }

    private <T extends Mergeable> T mergeMergeable(Class<? extends T> targetClass, T target, T source) throws MergerException {

        try {

            BeanInfo beanInfo = Introspector.getBeanInfo(targetClass);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method getter = propertyDescriptor.getReadMethod();
                Method setter = propertyDescriptor.getWriteMethod();

                String propertyName = propertyDescriptor.getDisplayName();

                if (PropertyUtils.isWriteable(target, propertyName)) {

                    //check for mergeCollection
                    MergeCollection mergeCollection = findAnnotation(MergeCollection.class, getter, setter);
                    if (Collection.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                        PropertyUtils.setProperty(target, propertyName, mergeList(mergeCollection, propertyName, target, source));
                    }

                    //check for merge
                    Merge mergeAnnotation = findAnnotation(Merge.class, getter, setter);
                    PropertyUtils.setProperty(target, propertyName, mergeProperties(mergeAnnotation, propertyName, target, source));
                }
            }
        } catch (NoSuchMethodException e) {
            throw new MergerException("NoSuchMethodException while trying to merge", e);
        } catch (IntrospectionException e) {
            throw new MergerException("IntrospectionException while trying to merge", e);
        } catch (IllegalAccessException e) {
            throw new MergerException("IllegalAccessException while trying to merge", e);
        } catch (InvocationTargetException e) {
            throw new MergerException("InvocationTargetException while trying to merge", e);
        }

        return target;
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

    private <T extends Mergeable> Object mergeProperties(Merge mergeAnnotation, String propertyName, T target, T source) throws MergerException {

        try {

            String tag = null;
            if (mergeAnnotation != null) {
                tag = mergeAnnotation.value();
            }

            Object targetProperty = PropertyUtils.getProperty(target, propertyName);
            Object sourceProperty = PropertyUtils.getProperty(source, propertyName);
            Class propertyType = PropertyUtils.getPropertyType(target, propertyName);

            Object merged;
            if (tag != null && target.isGenerated() && target.containsTag(tag)) {
                merged = sourceProperty;
            } else {
                merged = merge(propertyType, targetProperty, sourceProperty);
            }

            updateTag(target, tag, merged == null);
            return merged;

        } catch (NoSuchMethodException e) {
            throw new MergerException("NoSuchMethodException while trying to merge", e);
        } catch (IllegalAccessException e) {
            throw new MergerException("IllegalAccessException while trying to merge", e);
        } catch (InvocationTargetException e) {
            throw new MergerException("InvocationTargetException while trying to merge", e);
        }
    }

    private <T extends Mergeable> void updateTag(T target, String tag, boolean remove) {
        if(tag != null){
            if(remove){
                target.removeMergeTag(tag);
            }
            else{
                target.addMergeTag(tag);
            }
        }
    }

    private <T extends Mergeable> List mergeList(MergeCollection mergeCollectionAnnotation, String propertyName, T target, T source) throws MergerException {

        try {

            List targetCollection = (List) PropertyUtils.getProperty(target, propertyName);
            List sourceCollection = (List) PropertyUtils.getProperty(source, propertyName);

            if (mergeCollectionAnnotation == null) {
                return (List) merge(PropertyUtils.getPropertyType(target, propertyName), targetCollection, sourceCollection);
            }

            //update collection from source
            Collection<Mergeable> merged = updateFromSource(targetCollection, sourceCollection, mergeCollectionAnnotation.type());

            List targetResult = makeCollection(targetCollection, mergeCollectionAnnotation.collectionType(), target, propertyName);

            targetResult.clear();
            targetResult.addAll(merged);

            return targetResult;

        } catch (NoSuchMethodException e) {
            throw new MergerException("NoSuchMethodException while trying to merge", e);
        } catch (IllegalAccessException e) {
            throw new MergerException("IllegalAccessException while trying to merge", e);
        } catch (InvocationTargetException e) {
            throw new MergerException("InvocationTargetException while trying to merge", e);
        }
    }

    private <T extends Mergeable> List makeCollection(List targetList, Class<? extends List> listType, T target, String propertyName) throws MergerException {

        try {
            //merger only supports Lists
            if (targetList == null) {
                //first look for specific impl in annotation
                if (listType != List.class) {
                    return listType.newInstance();
                } else {
                    //try to instantiate field type
                    return (List) PropertyUtils.getPropertyType(target, propertyName).newInstance();
                }
            }

            return targetList;
        } catch (NoSuchMethodException e) {
            throw new MergerException("NoSuchMethodException while trying to merge", e);
        } catch (IllegalAccessException e) {
            throw new MergerException("IllegalAccessException while trying to merge", e);
        } catch (InstantiationException e) {
            throw new MergerException("InstantiationException while trying to merge", e);
        } catch (InvocationTargetException e) {
            throw new MergerException("InvocationTargetException while trying to merge", e);
        }
    }

    private List<Mergeable> updateFromSource(List targetList, List sourceList, Class<? extends Mergeable> type) throws MergerException {

        Map<Object, Mergeable> targetMap = buildIdentifierMap(targetList);
        Map<Object, Mergeable> sourceMap = buildIdentifierMap(sourceList);
        Set<Object> originalTargetKeys = new HashSet<Object>(targetMap.keySet());

        try {
            //update
            for (Map.Entry<Object, Mergeable> mergeableSourceEntry : sourceMap.entrySet()) {

                Object sourceKey = mergeableSourceEntry.getKey();

                if (targetMap.containsKey(sourceKey)) {
                    //replace
                    Mergeable targetValue = targetMap.get(sourceKey);
                    if (targetValue.isGenerated()) {
                        targetMap.put(sourceKey, merge(type, targetValue, mergeableSourceEntry.getValue()));
                    }
                } else {
                    targetMap.put(sourceKey, merge(type, type.newInstance(), mergeableSourceEntry.getValue()));
                }
                originalTargetKeys.remove(sourceKey);
            }

            //remove the targets were not updated
            for (Object targetKey : originalTargetKeys) {
                Mergeable mergeable = targetMap.get(targetKey);

                if (mergeable.isGenerated()) {
                    targetMap.remove(targetKey);
                }
            }
        } catch (IllegalAccessException e) {
            throw new MergerException("IllegalAccessException while trying to merge", e);
        } catch (InstantiationException e) {
            throw new MergerException("InstantiationException while trying to merge!", e);
        }

        //order should e preserved by LinkedHashMap
        return new ArrayList<Mergeable>(targetMap.values());
    }

    private Map<Object, Mergeable> buildIdentifierMap(List input) {
        Map<Object, Mergeable> mergeable = new LinkedHashMap<Object, Mergeable>();

        if (input != null) {
            int i = 0;
            for (Object o : input) {
                //validate all instance are of type Mergeable
                if (!(o instanceof Mergeable)) {
                    throw new TransfuseAnalysisException("Merge collection failed on type: " + o.getClass().getName());
                }
                Mergeable t = (Mergeable) o;
                Object key;
                if(t instanceof Identified){
                    key = ((Identified)t).getIdentifier();
                }
                else{
                    key = i;
                }
                mergeable.put(key, t);
                i++;
            }
        }

        return mergeable;
    }
}
