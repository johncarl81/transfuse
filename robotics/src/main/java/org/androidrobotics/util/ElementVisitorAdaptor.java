package org.androidrobotics.util;

import javax.lang.model.element.*;

/**
 * @author John Ericksen
 */
public class ElementVisitorAdaptor<T, R> implements ElementVisitor<T, R> {

    @Override
    public T visit(Element element, R r) {
        return null;
    }

    @Override
    public T visit(Element element) {
        return null;
    }

    @Override
    public T visitPackage(PackageElement packageElement, R r) {
        return null;
    }

    @Override
    public T visitType(TypeElement typeElement, R r) {
        return null;
    }

    @Override
    public T visitVariable(VariableElement variableElement, R r) {
        return null;
    }

    @Override
    public T visitExecutable(ExecutableElement executableElement, R r) {
        return null;
    }

    @Override
    public T visitTypeParameter(TypeParameterElement typeParameterElement, R r) {
        return null;
    }

    @Override
    public T visitUnknown(Element element, R r) {
        return null;
    }
}
