/**
 * Copyright 2011-2015 John Ericksen
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
public class UsesConfiguration extends ManifestBase {

    private Boolean reqFiveWayNav;
    private Boolean reqHardKeyboard;
    private ReqKeyboardType reqKeyboardType;
    private ReqNavigation reqNavigation;
    private ReqTouchScreen reqTouchScreen;

    @XmlAttribute(name = "reqFiveWayNav", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getReqFiveWayNav() {
        return reqFiveWayNav;
    }

    public void setReqFiveWayNav(Boolean reqFiveWayNav) {
        this.reqFiveWayNav = reqFiveWayNav;
    }

    @XmlAttribute(name = "reqHardKeyboard", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getReqHardKeyboard() {
        return reqHardKeyboard;
    }

    public void setReqHardKeyboard(Boolean reqHardKeyboard) {
        this.reqHardKeyboard = reqHardKeyboard;
    }

    @XmlAttribute(name = "reqKeyboardType", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.ReqKeyboardTypeConverter.class)
    public ReqKeyboardType getReqKeyboardType() {
        return reqKeyboardType;
    }

    public void setReqKeyboardType(ReqKeyboardType reqKeyboardType) {
        this.reqKeyboardType = reqKeyboardType;
    }

    @XmlAttribute(name = "reqNavigation", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.ReqNavigationConverter.class)
    public ReqNavigation getReqNavigation() {
        return reqNavigation;
    }

    public void setReqNavigation(ReqNavigation reqNavigation) {
        this.reqNavigation = reqNavigation;
    }

    @XmlAttribute(name = "reqTouchScreen", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.ReqTouchScreenConverter.class)
    public ReqTouchScreen getReqTouchScreen() {
        return reqTouchScreen;
    }

    public void setReqTouchScreen(ReqTouchScreen reqTouchScreen) {
        this.reqTouchScreen = reqTouchScreen;
    }
}
