package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public interface CollectionConverter<T, V> {
    V convert(T t);
}
