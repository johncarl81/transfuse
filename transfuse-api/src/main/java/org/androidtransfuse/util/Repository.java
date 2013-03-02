package org.androidtransfuse.util;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface Repository<T> {

    Map<Class, T> get();
}
