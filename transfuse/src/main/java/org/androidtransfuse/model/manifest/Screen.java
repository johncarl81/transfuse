package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes:
 * android:screenSize=["small" | "normal" | "large" | "xlarge"]
 * android:screenDensity=["ldpi" | "mdpi" | "hdpi" | "xhdpi"]
 *
 * @author John Ericksen
 */
public class Screen {

    @XStreamAlias("android:screenSize")
    @XStreamAsAttribute
    private ScreenSize screenSize;
    @XStreamAlias("android:screenDensity")
    @XStreamAsAttribute
    private ScreenDensity screenDensity;

    public ScreenSize getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(ScreenSize screenSize) {
        this.screenSize = screenSize;
    }

    public ScreenDensity getScreenDensity() {
        return screenDensity;
    }

    public void setScreenDensity(ScreenDensity screenDensity) {
        this.screenDensity = screenDensity;
    }
}
