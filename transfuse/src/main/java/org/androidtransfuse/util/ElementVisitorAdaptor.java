package org.androidtransfuse.util;

import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor6;

/**
 * @author John Ericksen
 */
public class ElementVisitorAdaptor<T, R> extends SimpleElementVisitor6<T, R> {

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
