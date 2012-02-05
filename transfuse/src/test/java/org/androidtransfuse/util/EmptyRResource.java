package org.androidtransfuse.util;

import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.ResourceIdentifier;

/**
 * @author John Ericksen
 */
public class EmptyRResource implements RResource {

    @Override
    public ResourceIdentifier getResourceIdentifier(Integer id) {
        return null;
    }
}
