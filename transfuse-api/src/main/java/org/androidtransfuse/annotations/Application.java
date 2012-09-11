package org.androidtransfuse.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a class to be a Transfuse Application component.  For classes that do not extent the android.app.Application
 * class this annotation activates the event systems, dependency injection features and manifest management.
 *
 * For classes that do extend the android.app.Application class, defining a class as a Transfuse Application will simply
 * activate manifest management of the Application.
 *
 * Under both cases you may define additional manifest metadata which will be associated with the Application manifest
 * entry.
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Application {

    String name() default "";

    String label() default "";

    boolean allowTaskReparenting() default false;

    String backupAgent() default "";

    boolean debuggable() default false;

    String description() default "";

    boolean enabled() default true;

    boolean hasCode() default true;

    boolean hardwareAccelerated() default false;

    String icon() default "";

    boolean killAfterRestore() default true;

    String logo() default "";

    String manageSpaceActivity() default "";

    String permission() default "";

    boolean persistent() default false;

    String process() default "";

    boolean restoreAnyVersion() default false;

    String taskAffinity() default "";

    String theme() default "";

    UIOptions uiOptions() default UIOptions.NONE;
}
