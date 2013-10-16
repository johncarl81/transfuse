package org.androidtransfuse.annotations;

/**
 * @author John Ericksen
 */
public @interface Permission {
    String name();

    String description() default "";

    String icon() default "";

    String label() default "";

    String permissionGroup() default "";

    ProtectionLevel protectionLevel() default ProtectionLevel.NORMAL;
}
