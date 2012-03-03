package org.androidtransfuse.analysis.adapter;

import java.util.List;

/**
 * @author John Ericksen
 */
public interface LazyTypeParameterBuilder {

    List<ASTType> buildGenericParameters();
}
