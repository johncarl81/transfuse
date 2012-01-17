package org.androidrobotics.model.r;

import org.androidrobotics.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class RResourceMapping implements RResource {

    private ASTType rType;
    private Map<Integer, ResourceIdentifier> resourceMap = new HashMap<Integer, ResourceIdentifier>();

    public RResourceMapping(ASTType rType) {
        this.rType = rType;
    }

    public void addResource(String innerClass, String name, Integer id) {
        resourceMap.put(id, new ResourceIdentifier(rType, innerClass, name));
    }

    public ResourceIdentifier getResourceIdentifier(Integer id) {
        return resourceMap.get(id);
    }
}
