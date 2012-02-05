package org.androidtransfuse.model.r;

import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.Collection;

/**
 * @author John Ericksen
 */
public class RBuilder {

    public RResourceMapping buildR(Collection<? extends ASTType> rInnerTypes) {

        RResourceMapping resourceMapping = new RResourceMapping();

        for (ASTType rInnerType : rInnerTypes) {

            for (ASTField idField : rInnerType.getFields()) {
                resourceMapping.addResource(rInnerType, idField.getName(), (Integer) idField.getConstantValue());
            }
        }

        return resourceMapping;
    }
}
