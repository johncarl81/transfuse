package org.androidrobotics.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author John Ericksen
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Intent {
    IntentType type();

    String name();
}
