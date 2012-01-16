package org.androidrobotics.config;

import com.thoughtworks.xstream.XStream;
import org.androidrobotics.model.manifest.*;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class XStreamProvider implements Provider<XStream> {
    @Override
    public XStream get() {
        XStream xStream = new XStream();

        xStream.processAnnotations(Manifest.class);

        xStream.registerConverter(new LabeledEnumConverter<ConfigChanges>(ConfigChanges.class, ConfigChanges.values()));
        xStream.registerConverter(new LabeledEnumConverter<InstallLocation>(InstallLocation.class, InstallLocation.values()));
        xStream.registerConverter(new LabeledEnumConverter<LaunchMode>(LaunchMode.class, LaunchMode.values()));
        xStream.registerConverter(new LabeledEnumConverter<ProtectionLevel>(ProtectionLevel.class, ProtectionLevel.values()));
        xStream.registerConverter(new LabeledEnumConverter<ReqKeyboardType>(ReqKeyboardType.class, ReqKeyboardType.values()));
        xStream.registerConverter(new LabeledEnumConverter<ReqNavigation>(ReqNavigation.class, ReqNavigation.values()));
        xStream.registerConverter(new LabeledEnumConverter<ReqTouchScreen>(ReqTouchScreen.class, ReqTouchScreen.values()));
        xStream.registerConverter(new LabeledEnumConverter<ScreenDensity>(ScreenDensity.class, ScreenDensity.values()));
        xStream.registerConverter(new LabeledEnumConverter<ScreenOrientation>(ScreenOrientation.class, ScreenOrientation.values()));
        xStream.registerConverter(new LabeledEnumConverter<ScreenSize>(ScreenSize.class, ScreenSize.values()));
        xStream.registerConverter(new LabeledEnumConverter<UIOptions>(UIOptions.class, UIOptions.values()));
        xStream.registerConverter(new LabeledEnumConverter<WindowSoftInputMode>(WindowSoftInputMode.class, WindowSoftInputMode.values()));

        return xStream;
    }
}
