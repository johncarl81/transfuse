package org.androidrobotics.processor;

/**
 * @author John Ericksen
 */
public interface Mergable<T> {

    T getIdentifier();

    void setMergeTag(String tag);

    String getMergeTag();

}
