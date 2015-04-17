package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTStringType;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.IntentFactoryStrategyGenerator;
import org.androidtransfuse.util.matcher.Matcher;

public class ParcelMatcher implements Matcher<ASTType> {

    private static final ASTType PARCEL_TYPE = new ASTStringType(IntentFactoryStrategyGenerator.ATPARCEL_NAME.getCanonicalName());

    @Override
    public boolean matches(ASTType type) {
        for (ASTAnnotation annotation : type.getAnnotations()) {
            if(annotation.getASTType().equals(PARCEL_TYPE)){
                return true;
            }
        }
        return false;
    }
}