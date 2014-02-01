package org.androidtransfuse.util.matcher;

import org.androidtransfuse.adapter.ASTArrayType;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.util.matcher.Matcher;import java.lang.Override;

/**
 * @author John Ericksen
 */
public class ASTArrayMatcher implements Matcher<ASTType> {

    @Override
    public boolean matches(ASTType input) {
        return input instanceof ASTArrayType;
    }
}
