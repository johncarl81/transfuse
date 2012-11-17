package org.androidtransfuse.util;

import java.lang.annotation.Annotation;

@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SupportedAnnotations {

    java.lang.Class<? extends Annotation>[] value();
}