package org.androidrobotics.model.r;

import org.androidrobotics.analysis.adapter.ASTField;
import org.androidrobotics.analysis.adapter.ASTType;

import java.util.Collection;

/**
 * @author John Ericksen
 */
public class RBuilder {

    public RResourceMapping buildR(ASTType rType, Collection<? extends ASTType> rInnerTypes) {

        RResourceMapping resourceMapping = new RResourceMapping(rType);

        for (ASTType rInnerType : rInnerTypes) {

            String rInnerTypeFullyQualifiedName = rInnerType.getName();
            String rInnerTypeName = rInnerTypeFullyQualifiedName.substring(rInnerTypeFullyQualifiedName.lastIndexOf('.') + 1);

            for (ASTField idField : rInnerType.getFields()) {
                resourceMapping.addResource(rInnerTypeName, idField.getName(), (Integer) idField.getConstantValue());
            }
        }

        return resourceMapping;
    }
}
