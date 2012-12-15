package org.androidtransfuse.annotations;

public enum ConfigChanges implements LabeledEnum {
    MCC("mcc"),
    MNC("mnc"),
    LOCALE("locale"),
    TOUCHSCREEN("touchscreen"),
    KEYBOARD("keyboard"),
    KEYBOARD_HIDDEN("keyboardHidden"),
    NAVIGATION("navigation"),
    SCREEN_LAYOUT("screenLayout"),
    FONT_SCALE("fontScale"),
    UI_MODE("uiMode"),
    ORIENTATION("orientation"),
    SCREEN_SIZE("screenSize"),
    SMALLEST_SCREENSIZE("smallestScreenSize");

    private final String label;

    private ConfigChanges(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}


    




