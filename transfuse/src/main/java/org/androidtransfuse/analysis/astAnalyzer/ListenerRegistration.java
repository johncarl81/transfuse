package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.model.InjectionNode;

import java.util.List;

/**
 * @author John Ericksen
 */
public class ListenerRegistration {

    private InjectionNode viewInjectionNode;
    private List<String> methods;
    private ASTField field;

    public ListenerRegistration(InjectionNode viewInjectionNode, List<String> methods, ASTField field) {
        this.viewInjectionNode = viewInjectionNode;
        this.methods = methods;
        this.field = field;
    }

    public InjectionNode getViewInjectionNode() {
        return viewInjectionNode;
    }

    public List<String> getMethods() {
        return methods;
    }

    public ASTField getField() {
        return field;
    }
}
