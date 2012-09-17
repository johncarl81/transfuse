package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ParcelableDescriptor {

    private final List<GetterSetterMethodPair> getterSetterPairs = new ArrayList<GetterSetterMethodPair>();
    private final ASTType parcelableConverterType;

    public ParcelableDescriptor() {
        parcelableConverterType = null;
    }

    public ParcelableDescriptor(ASTType parcelableConverterType) {
        this.parcelableConverterType = parcelableConverterType;
    }

    public List<GetterSetterMethodPair> getGetterSetterPairs() {
        return getterSetterPairs;
    }

    public ASTType getParcelableConverterType() {
        return parcelableConverterType;
    }
}
