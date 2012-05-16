package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.model.InjectionNode;

import java.util.List;

/**
 * @author John Ericksen
 */
public class RegistrationAspect {

    private InjectionNode viewInjectionNode;
    private List<String> methods;
    private ASTField field;

    public RegistrationAspect(InjectionNode viewInjectionNode, ASTField astField, List<String> methods) {
        this.viewInjectionNode = viewInjectionNode;
        this.methods = methods;
        this.field = astField;
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
