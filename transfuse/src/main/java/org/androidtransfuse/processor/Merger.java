package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
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
                        PropertyUtils.setProperty(target, propertyName, mergeCollections(mergeCollection, propertyName, target, source));
                    }

                    //check for merge
                    Merge mergeAnnotation = findAnnotation(Merge.class, getter, setter);
                    PropertyUtils.setProperty(target, propertyName, mergeProperties(mergeAnnotation, propertyName, target, source));
                }
            }
        } catch (NoSuchMethodException e) {
            throw new MergerException("Error while trying to merge", e);
        } catch (IntrospectionException e) {
            throw new MergerException("Error while trying to merge", e);
        } catch (IllegalAccessException e) {
            throw new MergerException("Error while trying to merge", e);
        } catch (InvocationTargetException e) {
            throw new MergerException("Error while trying to merge", e);
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

            if (tag != null && target.getMergeTags().contains(tag)) {
                //merge away
                return merge(propertyType, null, sourceProperty);
            } else {
                if (tag != null) {
                    target.addMergeTag(tag);
                }
                return merge(propertyType, targetProperty, sourceProperty);
            }

        } catch (NoSuchMethodException e) {
            throw new MergerException("Error while trying to merge", e);
        } catch (IllegalAccessException e) {
            throw new MergerException("Error while trying to merge", e);
        } catch (InvocationTargetException e) {
            throw new MergerException("Error while trying to merge", e);
        }
    }

    private <T extends Mergeable> Collection mergeCollections(MergeCollection mergeCollectionAnnotation, String propertyName, T target, T source) throws MergerException {

        try {

            Collection targetCollection = (Collection) PropertyUtils.getProperty(target, propertyName);
            Collection sourceCollection = (Collection) PropertyUtils.getProperty(source, propertyName);

            if (mergeCollectionAnnotation == null) {
                return (Collection) merge(PropertyUtils.getPropertyType(target, propertyName), targetCollection, sourceCollection);
            }

            Map<Object, Mergeable> targetMap = convertToMergable(targetCollection);
            Map<Object, Mergeable> sourceMap = convertToMergable(sourceCollection);
            Set<Object> originalTargetKeys = new HashSet<Object>(targetMap.keySet());

            //update
            for (Map.Entry<Object, Mergeable> mergableSourceEntry : sourceMap.entrySet()) {

                Object sourceKey = mergableSourceEntry.getKey();

                if (targetMap.containsKey(sourceKey)) {
                    //replace
                    Mergeable targetValue = targetMap.get(sourceKey);
                    if (targetValue.getMergeTags() != null && !targetValue.getMergeTags().isEmpty()) {
                        targetMap.put(sourceKey, merge(mergeCollectionAnnotation.type(), targetValue, mergableSourceEntry.getValue()));
                    }
                } else {
                    targetMap.put(sourceKey, merge(mergeCollectionAnnotation.type(), mergeCollectionAnnotation.type().newInstance(), mergableSourceEntry.getValue()));
                }
                originalTargetKeys.remove(sourceKey);
            }

            //figure out what targets were not updated
            for (Object targetKey : originalTargetKeys) {
                Mergeable mergable = targetMap.get(targetKey);

                if (mergable.getMergeTags() != null && !mergable.getMergeTags().isEmpty()) {
                    targetMap.remove(targetKey);
                }
            }

            Collection targetResult = targetCollection;
            //merger only supports Lists for collections
            if (targetResult == null) {
                //first look for specific impl in annotation
                if (mergeCollectionAnnotation.collectionType() != Collection.class) {
                    targetResult = mergeCollectionAnnotation.collectionType().newInstance();
                } else {
                    //try to instantiate field type
                    targetResult = (Collection) PropertyUtils.getPropertyType(target, propertyName).newInstance();
                }
            }

            targetResult.clear();
            targetResult.addAll(targetMap.values());

            return targetResult;

        } catch (NoSuchMethodException e) {
            throw new MergerException("Error while trying to merge", e);
        } catch (IllegalAccessException e) {
            throw new MergerException("Error while trying to merge", e);
        } catch (InstantiationException e) {
            throw new MergerException("Error while trying to merge", e);
        } catch (InvocationTargetException e) {
            throw new MergerException("Error while trying to merge", e);
        }
    }

    private Map<Object, Mergeable> convertToMergable(Collection input) {
        Map<Object, Mergeable> mergeable = new HashMap<Object, Mergeable>();

        if (input != null) {
            for (Object o : input) {
                //validate all instance are of type Mergeable
                if (!(o instanceof Mergeable)) {
                    throw new TransfuseAnalysisException("Merge collection failed on type: " + o.getClass().getName());
                }
                Mergeable t = (Mergeable) o;
                mergeable.put(t.getIdentifier(), t);

            }
        }

        return mergeable;
    }
}
