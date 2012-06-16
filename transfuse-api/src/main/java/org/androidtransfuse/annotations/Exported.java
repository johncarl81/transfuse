package org.androidtransfuse.annotations;

/**
 * @author John Ericksen
 */
public enum Exported {
    UNSPECIFIED(null),
    TRUE(true),
    FALSE(false);

    private Boolean value;

    private Exported(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
