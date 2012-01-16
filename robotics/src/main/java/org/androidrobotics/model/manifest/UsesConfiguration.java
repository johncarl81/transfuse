package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author John Ericksen
 */
public class UsesConfiguration {

    @XStreamAlias("android:reqFiveWayNav")
    @XStreamAsAttribute
    private Boolean reqFiveWayNav;
    @XStreamAlias("android:reqHardKeyboard")
    @XStreamAsAttribute
    private Boolean reqHardKeyboard;
    @XStreamAlias("android:reqKeybaordType")
    @XStreamAsAttribute
    private ReqKeybaordType reqKeybaordType;
    @XStreamAlias("android:reqNavigation")
    @XStreamAsAttribute
    private ReqNavigation reqNavigation;
    @XStreamAlias("android:reqTouchScreen")
    @XStreamAsAttribute
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
