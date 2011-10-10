package org.androidrobotics.analysis;

/**
 * @author John Ericksen
 */
public interface AnalysisBridgeVisitor {
    void visit(ElementAnalysisBridge elementAnalysisBridge);

    void visit(TypeElementAnalysisBridge typeElementAnalysisBridge);
}
