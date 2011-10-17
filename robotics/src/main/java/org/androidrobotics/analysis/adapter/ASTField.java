package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public interface ASTField {
    boolean isAnnotated(Class<Inject> annotation);

    ASTType getASTType();

    String getName();

}
