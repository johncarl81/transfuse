package org.androidrobotics.util;

import org.androidrobotics.model.r.RResource;
import org.androidrobotics.model.r.ResourceIdentifier;

/**
 * @author John Ericksen
 */
public class EmptyRResource implements RResource {

    @Override
    public ResourceIdentifier getResourceIdentifier(Integer id) {
        return null;
    }
}
