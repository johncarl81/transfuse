package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes:
 * android:reqFiveWayNav=["true" | "false"]
 * android:reqHardKeyboard=["true" | "false"]
 * android:reqKeyboardType=["undefined" | "nokeys" | "qwerty" |
 * "twelvekey"]
 * android:reqNavigation=["undefined" | "nonav" | "dpad" |
 * "trackball" | "wheel"]
 * android:reqTouchScreen=["undefined" | "notouch" | "stylus" |
 * "finger"]
 *
 * @author John Ericksen
 */
public class UsesConfiguration {

    @XStreamAlias("android:reqFiveWayNav")
    @XStreamAsAttribute
    private Boolean reqFiveWayNav;
    @XStreamAlias("android:reqHardKeyboard")
    @XStreamAsAttribute
    private Boolean reqHardKeyboard;
    @XStreamAlias("android:reqKeyboardType")
    @XStreamAsAttribute
    private ReqKeyboardType reqKeyboardType;
    @XStreamAlias("android:reqNavigation")
    @XStreamAsAttribute
    private ReqNavigation reqNavigation;
    @XStreamAlias("android:reqTouchScreen")
    @XStreamAsAttribute
    private ReqTouchScreen reqTouchScreen;

    public Boolean getReqFiveWayNav() {
        return reqFiveWayNav;
    }

    public void setReqFiveWayNav(Boolean reqFiveWayNav) {
        this.reqFiveWayNav = reqFiveWayNav;
    }

    public Boolean getReqHardKeyboard() {
        return reqHardKeyboard;
    }

    public void setReqHardKeyboard(Boolean reqHardKeyboard) {
        this.reqHardKeyboard = reqHardKeyboard;
    }

    public ReqKeyboardType getReqKeyboardType() {
        return reqKeyboardType;
    }

    public void setReqKeyboardType(ReqKeyboardType reqKeyboardType) {
        this.reqKeyboardType = reqKeyboardType;
    }

    public ReqNavigation getReqNavigation() {
        return reqNavigation;
    }

    public void setReqNavigation(ReqNavigation reqNavigation) {
        this.reqNavigation = reqNavigation;
    }

    public ReqTouchScreen getReqTouchScreen() {
        return reqTouchScreen;
    }

    public void setReqTouchScreen(ReqTouchScreen reqTouchScreen) {
        this.reqTouchScreen = reqTouchScreen;
    }
}
