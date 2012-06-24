package org.androidtransfuse.processor;

import org.androidtransfuse.model.Mergeable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeCollection {
    Class<? extends List> collectionType();

    Class<? extends Mergeable> type();
}