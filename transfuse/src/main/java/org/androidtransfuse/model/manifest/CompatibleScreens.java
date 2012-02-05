package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * child elements:
 * <screen>
 *
 * @author John Ericksen
 */
public class CompatibleScreens {

    @XStreamImplicit(itemFieldName = "screen")
    private List<Screen> screens = new ArrayList<Screen>();

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }
}
