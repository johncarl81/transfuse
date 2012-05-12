package org.androidtransfuse.util;

/**
 * Copy of the Java Generated annotation (absent in Android implementation)
 *
 * @author John Ericksen
 */
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
@java.lang.annotation.Target({java.lang.annotation.ElementType.PACKAGE, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.PARAMETER})
public @interface Generated {

    java.lang.String[] value();

    java.lang.String date() default "";

    java.lang.String comments() default "";
}
