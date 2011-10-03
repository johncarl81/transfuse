package org.androidrobotics.analysis;

import java.lang.reflect.AnnotatedElement;

/**
 * @author John Ericksen
 */
public interface RoboticsAnalysis<T> {

    T analyzeElement(AnnotatedElement input);

    Class<? extends T> getTargetDescriptor();

}
