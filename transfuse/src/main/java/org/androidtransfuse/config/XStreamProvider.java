package org.androidtransfuse.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.model.manifest.*;
import org.androidtransfuse.processor.MergeableTagConverter;

import javax.inject.Provider;
import java.io.Writer;

/**
 * @author John Ericksen
 */
public class XStreamProvider implements Provider<XStream> {
    @Override
    public XStream get() {
        XStream xStream = new XStream(new FourSpaceTabXppDriver());

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
        xStream.registerConverter(new MergeableTagConverter());

        return xStream;
    }

    private static final class FourSpaceTabXppDriver extends XppDriver {

        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out, PrettyPrintWriter.XML_QUIRKS, "    ".toCharArray(), super.getNameCoder());
        }
    }
}
