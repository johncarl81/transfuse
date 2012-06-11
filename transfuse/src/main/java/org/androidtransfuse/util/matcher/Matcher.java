package org.androidtransfuse.util.matcher;

/**
 * @author John Ericksen
 */
public interface Matcher<T> {

    boolean matches(T astType);
}
