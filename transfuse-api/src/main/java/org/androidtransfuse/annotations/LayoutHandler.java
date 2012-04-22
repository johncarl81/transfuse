package org.androidtransfuse.annotations;

import org.androidtransfuse.layout.LayoutHandlerDelegate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutHandler {
    Class<? extends LayoutHandlerDelegate> value();
}
