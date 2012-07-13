package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTBase;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.model.InjectionNode;

import java.util.List;

/**
 * Listener registration associating an InjectionNode with the series of methods that will be wired to the given
 * triggering method.
 *
 * @author John Ericksen
 */
public class ActionRegistration<T extends ASTBase> {

    private InjectionNode injectionNode;
    private List<ASTMethod> methods;
    private T astBase;

    public ActionRegistration(InjectionNode injectionNode, List<ASTMethod> methods, T astBase) {
        this.injectionNode = injectionNode;
        this.methods = methods;
        this.astBase = astBase;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }

    public List<ASTMethod> getMethods() {
        return methods;
    }

    public T getASTBase() {
        return astBase;
    }
}
