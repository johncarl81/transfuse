package org.androidtransfuse.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a class to be a Transfuse Service component.  For classes that do not extent the android.app.Service
 * class this annotation activates the event systems, dependency injection features and manifest management.
 *
 * For classes that do extend the android.app.Service class, defining a class as a Transfuse Service will simply
 * activate manifest management of the Application.
 *
 * Under both cases you may define additional manifest metadata which will be associated with the Service manifest
 * entry.
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    String name() default "";

    String label() default "";

    boolean enabled() default true;

    boolean exported() default true;

    String icon() default "";

    String permission() default "";

    String process() default "";

    Class<? extends android.app.Service> type() default android.app.Service.class;
}
