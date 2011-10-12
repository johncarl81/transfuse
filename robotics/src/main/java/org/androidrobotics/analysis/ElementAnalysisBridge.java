package org.androidrobotics.analysis;

import javax.lang.model.element.Element;

/**
 * @author John Ericksen
 */
public class ElementAnalysisBridge<T extends Element> extends ElementAnalysisBridgeBase<T> {

    public ElementAnalysisBridge(T element) {
        super(element);
    }

    @Override
    public void accept(AnalysisBridgeVisitor visitor) {
        visitor.visit(this);
    }
}
