package org.androidtransfuse.model.r;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class RResourceComposite implements RResource {

    private final ImmutableList<RResource> resources;

    public RResourceComposite(RResource... resources) {
        this.resources = FluentIterable
                .from(Arrays.asList(resources))
                .filter(Predicates.notNull())
                .toImmutableList();
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
