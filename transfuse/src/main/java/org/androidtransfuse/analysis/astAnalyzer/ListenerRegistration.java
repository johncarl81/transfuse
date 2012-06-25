package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTBase;
import org.androidtransfuse.model.InjectionNode;

import java.util.List;

/**
 * @author John Ericksen
 */
public class ListenerRegistration<T extends ASTBase> {

    private InjectionNode viewInjectionNode;
    private List<String> methods;
    private T astBase;
    private int level;

    public ListenerRegistration(InjectionNode viewInjectionNode, List<String> methods, T astBase, int level) {
        this.viewInjectionNode = viewInjectionNode;
        this.methods = methods;
        this.astBase = astBase;
        this.level = level;
    }

    public InjectionNode getViewInjectionNode() {
        return viewInjectionNode;
    }

    public List<String> getMethods() {
        return methods;
    }

    public T getASTBase() {
        return astBase;
    }

    public int getLevel(){
        return level;
    }
}
