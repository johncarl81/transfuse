package org.androidtransfuse.matcher;

import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public interface Match {

    boolean matches(ASTType astType);
}
