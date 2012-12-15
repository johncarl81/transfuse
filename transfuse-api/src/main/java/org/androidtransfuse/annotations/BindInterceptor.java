package org.androidtransfuse.annotations;

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.annotation.*;

/**
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindInterceptor {
    Class<? extends Annotation> annotation();

    Class<? extends MethodInterceptor> interceptor();
}
