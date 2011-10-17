package org.androidrobotics.util;

/**
 * @author John Ericksen
 */
public interface CollectionConverter<T, V> {
    V convert(T t);
}
