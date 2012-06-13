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
