package org.androidrobotics.annotations;

import java.lang.annotation.*;

/**
 * @author John Ericksen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindInterceptor {
    Class<? extends Annotation> value();
}
