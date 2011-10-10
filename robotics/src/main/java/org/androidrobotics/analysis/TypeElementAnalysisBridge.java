package org.androidrobotics.analysis;

import javax.lang.model.element.TypeElement;

/**
 * @author John Ericksen
 */
public class TypeElementAnalysisBridge extends ElementAnalysisBridgeBase<TypeElement> {

    public TypeElementAnalysisBridge(TypeElement element) {
        super(element);
    }

    @Override
    public String getName() {
        return getElement().getQualifiedName().toString();
    }

    @Override
    public void accept(AnalysisBridgeVisitor visitor) {
        visitor.visit(this);
    }
}
