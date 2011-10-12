package org.androidrobotics.analysis;

/**
 * @author John Ericksen
 */
public interface RoboticsAnalysis<T> {

    T analyzeElement(TypeAnalysisBridge input);

    Class<? extends T> getTargetDescriptor();

}
