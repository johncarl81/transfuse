/**
 * Copyright 2013 John Ericksen
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

import org.androidtransfuse.annotations.*;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class LabeledConverter<T extends Labeled> extends XmlAdapter<String, T> {

    public static final class ConfigChangesConverter extends LabeledConverter<ConfigChanges>{
        public ConfigChangesConverter() {
            super(ConfigChanges.class, ConfigChanges.values());
        }
    }

    public static final class InstallLocationConverter extends LabeledConverter<InstallLocation>{
        public InstallLocationConverter() {
            super(InstallLocation.class, InstallLocation.values());
        }
    }

    public static final class LaunchModeConverter extends LabeledConverter<LaunchMode>{
        public LaunchModeConverter() {
            super(LaunchMode.class, LaunchMode.values());
        }
    }

    public static final class ProtectionLevelConverter extends LabeledConverter<ProtectionLevel>{
        public ProtectionLevelConverter() {
            super(ProtectionLevel.class, ProtectionLevel.values());
        }
    }

    public static final class ReqKeyboardTypeConverter extends LabeledConverter<ReqKeyboardType>{
        public ReqKeyboardTypeConverter() {
            super(ReqKeyboardType.class, ReqKeyboardType.values());
        }
    }

    public static final class ReqNavigationConverter extends LabeledConverter<ReqNavigation>{
        public ReqNavigationConverter() {
            super(ReqNavigation.class, ReqNavigation.values());
        }
    }

    public static final class ReqTouchScreenConverter extends LabeledConverter<ReqTouchScreen>{
        public ReqTouchScreenConverter() {
            super(ReqTouchScreen.class, ReqTouchScreen.values());
        }
    }

    public static final class ScreenDensityConverter extends LabeledConverter<ScreenDensity>{
        public ScreenDensityConverter() {
            super(ScreenDensity.class, ScreenDensity.values());
        }
    }

    public static final class ScreenOrientationConverter extends LabeledConverter<ScreenOrientation>{
        public ScreenOrientationConverter() {
            super(ScreenOrientation.class, ScreenOrientation.values());
        }
    }

    public static final class ScreenSizeConverter extends LabeledConverter<ScreenSize>{
        public ScreenSizeConverter() {
            super(ScreenSize.class, ScreenSize.values());
        }
    }

    public static final class UIOptionsConverter extends LabeledConverter<UIOptions>{
        public UIOptionsConverter() {
            super(UIOptions.class, UIOptions.values());
        }
    }

    public static final class WindowSoftInputModeConverter extends LabeledConverter<WindowSoftInputMode>{
        public WindowSoftInputModeConverter() {
            super(WindowSoftInputMode.class, WindowSoftInputMode.values());
        }
    }

    private final Class<T> labeled;
    private final Map<String, T> labelMap;

    public LabeledConverter(Class<T> labeled, T[] values) {
        this.labeled = labeled;
        labelMap = new HashMap<String, T>();
        for (T value : values) {
            labelMap.put(value.getLabel(), value);
        }
    }

    @Override
    public String marshal(T obj) throws Exception {
        if(obj != null){
            return labeled.cast(obj).getLabel();
        }
        return null;
    }

    @Override
    public T unmarshal(String label) throws Exception {
        if(labelMap.containsKey(label)){
            return labelMap.get(label);
        }
        return null;
    }
}
