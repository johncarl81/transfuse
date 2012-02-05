package org.androidtransfuse.model.r;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author John Ericksen
 */
public class RResourceComposite implements RResource {

    private List<RResource> resources = new ArrayList<RResource>();

    public RResourceComposite(RResource... resources) {
        if (resources != null) {
            this.resources.addAll(Arrays.asList(resources));
        }
    }

    @Override
    public ResourceIdentifier getResourceIdentifier(Integer id) {

        for (RResource resource : resources) {
            ResourceIdentifier identifier = resource.getResourceIdentifier(id);
            if (identifier != null) {
                return identifier;
            }
        }
        return null;
    }
}
