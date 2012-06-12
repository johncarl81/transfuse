package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public interface Analysis<T> {

    T analyze(ASTType type);
}
