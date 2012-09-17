package org.androidtransfuse.model.r;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class RResourceComposite implements RResource {

    private final ImmutableList<RResource> resources;

    public RResourceComposite(RResource... resources) {
        ImmutableList.Builder<RResource> resourceBuilder = ImmutableList.builder();
        if (resources != null) {
            resourceBuilder.addAll(Arrays.asList(resources)).build();
        }
        this.resources = resourceBuilder.build();
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
