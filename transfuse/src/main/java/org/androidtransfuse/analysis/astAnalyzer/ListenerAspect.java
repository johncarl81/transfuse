package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTMethodUniqueSignatureDecorator;

import java.util.*;

/**
 * Aspect associating listenable methods with the set of listening methods.
 *
 * @author John Ericksen
 */
public class ListenerAspect {

    private Map<String, Set<ASTMethod>> listeners = new HashMap<String, Set<ASTMethod>>();

    public void addMethodCallback(String name, ASTMethod method) {
        if (!listeners.containsKey(name)) {
            listeners.put(name, new HashSet<ASTMethod>());
        }
        Set<ASTMethod> methods = listeners.get(name);

        methods.add(new ASTMethodUniqueSignatureDecorator(method));
    }

    public Set<ASTMethod> getListeners(String name) {
        if(listeners.containsKey(name)){
            return listeners.get(name);
        }

        return Collections.emptySet();
    }

    public boolean contains(String name) {
        return listeners.containsKey(name);
    }
}
