package org.androidrobotics.analysis;

import javax.lang.model.element.VariableElement;

/**
 * @author John Ericksen
 */
public class VariableElementAnalysisBridge extends ElementAnalysisBridgeBase<VariableElement> implements TypeAnalysisBridge {


    public VariableElementAnalysisBridge(VariableElement element) {
        super(element);
    }

    public String getName() {
        return getElement().asType().toString();
    }

    @Override
    public void accept(AnalysisBridgeVisitor visitor) {
        visitor.visit(this);
    }
}
