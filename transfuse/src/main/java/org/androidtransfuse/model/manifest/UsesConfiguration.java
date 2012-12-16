/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
