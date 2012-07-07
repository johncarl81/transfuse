package org.androidtransfuse.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
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
