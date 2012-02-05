package org.androidtransfuse.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeCollection {
    Class<? extends Collection> targetType() default Collection.class;
}