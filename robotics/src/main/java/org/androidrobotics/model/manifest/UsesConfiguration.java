package org.androidrobotics.model.manifest;

/**
 * @author John Ericksen
 */
public class UsesConfiguration {
    private Boolean reqFiveWayNav;
    private Boolean reqHardKeyboard;
    private ReqKeybaordType reqKeybaordType;
    private ReqNavigation reqNavigation;
    private ReqTouchScreen reqTouchScreen;

    /*
   android:reqFiveWayNav=["true" | "false"]
                   android:reqHardKeyboard=["true" | "false"]
                   android:reqKeyboardType=["undefined" | "nokeys" | "qwerty" |
                                            "twelvekey"]
                   android:reqNavigation=["undefined" | "nonav" | "dpad" |
                                          "trackball" | "wheel"]
                   android:reqTouchScreen=["undefined" | "notouch" | "stylus" |
                                           "finger"]
    */

}
