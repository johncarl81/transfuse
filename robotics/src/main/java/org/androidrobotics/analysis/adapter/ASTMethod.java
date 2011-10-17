package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;
import java.util.List;

/**
 * @author John Ericksen
 */
public interface ASTMethod {
    boolean isAnnotated(Class<Inject> annotation);

    String getName();

    List<ASTParameter> getParameters();
}
