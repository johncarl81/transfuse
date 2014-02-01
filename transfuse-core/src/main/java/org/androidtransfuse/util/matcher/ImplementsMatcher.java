package org.androidtransfuse.util.matcher;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.util.matcher.Matcher;import java.lang.Override;

/**
* @author John Ericksen
*/
public class ImplementsMatcher implements Matcher<ASTType> {

    private final ASTType superType;

    public ImplementsMatcher(ASTType superType) {
        this.superType = superType;
    }

    @Override
    public boolean matches(ASTType astType) {
        return astType.implementsFrom(superType);
    }
}
