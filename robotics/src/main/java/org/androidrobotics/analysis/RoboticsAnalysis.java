package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public interface RoboticsAnalysis<T> {

    T analyzeElement(ASTType input);

    Class<? extends T> getTargetDescriptor();

}
