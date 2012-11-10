package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ParcelableDescriptor {

    private final List<GetterSetterMethodPair> getterSetterPairs = new ArrayList<GetterSetterMethodPair>();
    private final ASTType parcelConverterType;

    public ParcelableDescriptor() {
        parcelConverterType = null;
    }

    public ParcelableDescriptor(ASTType parcelConverterType) {
        this.parcelConverterType = parcelConverterType;
    }

    public List<GetterSetterMethodPair> getGetterSetterPairs() {
        return getterSetterPairs;
    }

    public ASTType getParcelConverterType() {
        return parcelConverterType;
    }
}
