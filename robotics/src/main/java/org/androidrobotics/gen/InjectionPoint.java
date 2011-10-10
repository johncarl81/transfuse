package org.androidrobotics.gen;

import org.androidrobotics.analysis.AnalysisBridge;

/**
 * @author John Ericksen
 */
public class InjectionPoint {

    private AnalysisBridge enclosedElement;

    public InjectionPoint(AnalysisBridge enclosedElement) {
        this.enclosedElement = enclosedElement;
    }

    public AnalysisBridge getEnclosedElement() {
        return enclosedElement;
    }
}
