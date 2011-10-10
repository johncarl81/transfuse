package org.androidrobotics.analysis;

import javax.lang.model.element.*;

/**
 * @author John Ericksen
 */
public class AnalysisBridgeWrapperVisitor implements ElementVisitor<AnalysisBridge, Void> {
    @Override
    public AnalysisBridge visit(Element element, Void aVoid) {
        return new ElementAnalysisBridge<Element>(element);
    }

    @Override
    public AnalysisBridge visit(Element element) {
        return new ElementAnalysisBridge<Element>(element);
    }

    @Override
    public AnalysisBridge visitPackage(PackageElement packageElement, Void aVoid) {
        return new ElementAnalysisBridge<PackageElement>(packageElement);
    }

    @Override
    public AnalysisBridge visitType(TypeElement typeElement, Void aVoid) {
        return new TypeElementAnalysisBridge(typeElement);
    }

    @Override
    public AnalysisBridge visitVariable(VariableElement variableElement, Void aVoid) {
        return new ElementAnalysisBridge<VariableElement>(variableElement);
    }

    @Override
    public AnalysisBridge visitExecutable(ExecutableElement executableElement, Void aVoid) {
        return new ElementAnalysisBridge<ExecutableElement>(executableElement);
    }

    @Override
    public AnalysisBridge visitTypeParameter(TypeParameterElement typeParameterElement, Void aVoid) {
        return new ElementAnalysisBridge<TypeParameterElement>(typeParameterElement);
    }

    @Override
    public AnalysisBridge visitUnknown(Element element, Void aVoid) {
        return new ElementAnalysisBridge<Element>(element);
    }
}
