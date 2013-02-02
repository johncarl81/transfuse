package org.androidtransfuse.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation representation of the Android Manifest uses-sdk XML tag.  Defining this annotation on a
 * @code @TransfuseModule} will trigger Transfuse to generate the corresponding entry in the Android Manifest
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UsesSdk {
    int min() default -1;
    int target() default -1;
    int max() default -1;
}
