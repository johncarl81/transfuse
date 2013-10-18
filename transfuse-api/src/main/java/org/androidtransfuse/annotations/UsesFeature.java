package org.androidtransfuse.annotations;

/**
 * @author Gustavo Matias
 */
public @interface UsesFeature {
	String name() default "";

	boolean required() default true;

	int glEsVersion() default -1;
}
