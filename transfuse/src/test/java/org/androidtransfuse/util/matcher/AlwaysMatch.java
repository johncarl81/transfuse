package org.androidtransfuse.util.matcher;

import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class AlwaysMatch implements Matcher<ASTType> {
    @Override
    public boolean matches(ASTType astType) {
        return true;
    }
}
