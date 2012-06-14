package org.androidtransfuse.annotations;

public enum ConfigChanges implements LabeledEnum {
    MCC("mcc"),
    MNC("mnc"),
    LOCALE("locale"),
    TOUCHSCREEN("touchscreen"),
    KEYBOARD("keyboard"),
    KEYBOARDHIDDEN("keyboardHidden"),
    NAVIGATION("navigation"),
    SCREENLAYOUT("screenLayout"),
    FONTSCALE("fontScale"),
    UIMODE("uiMode"),
    ORIENTATION("orientation"),
    SCREENSIZE("screenSize"),
    SMALLESTSCREENSIZE("smallestScreenSize");

    private String label;

    private ConfigChanges(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}


    




