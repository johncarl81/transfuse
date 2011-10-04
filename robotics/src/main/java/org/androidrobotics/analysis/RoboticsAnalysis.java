package org.androidrobotics.analysis;

/**
 * @author John Ericksen
 */
public interface RoboticsAnalysis<T> {

    T analyzeElement(AnalysisBridge input);

    Class<? extends T> getTargetDescriptor();

}
