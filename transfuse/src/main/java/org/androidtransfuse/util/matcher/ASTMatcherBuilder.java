package org.androidtransfuse.util.matcher;

/**
 * @author John Ericksen
 */
public class ASTMatcherBuilder {

    public ASTTypeMatcherBuilder type() {
        return new ASTTypeMatcherBuilder();
    }
}
