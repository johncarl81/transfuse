package org.androidtransfuse.model.r;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class RResourceMapping implements RResource {

    private Map<Integer, ResourceIdentifier> resourceMap = new HashMap<Integer, ResourceIdentifier>();

    public void addResource(ASTType rInnerType, String name, Integer id) {
        resourceMap.put(id, new ResourceIdentifier(rInnerType, name));
    }

    public ResourceIdentifier getResourceIdentifier(Integer id) {
        return resourceMap.get(id);
    }
}
