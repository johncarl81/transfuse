package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTMethodUniqueSignatureDecorator;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Aspect associating listenable methods with the set of listening methods.
 *
 * @author John Ericksen
 */
public class ListenerAspect {

    private final Map<Class<? extends Annotation>, Set<ASTMethod>> listeners = new HashMap<Class<? extends Annotation>, Set<ASTMethod>>();

    public void addMethodCallback(Class<? extends Annotation> annotation, ASTMethod method) {
        if (!listeners.containsKey(annotation)) {
            listeners.put(annotation, new HashSet<ASTMethod>());
        }
        Set<ASTMethod> methods = listeners.get(annotation);

        methods.add(new ASTMethodUniqueSignatureDecorator(method));
    }

    public Set<ASTMethod> getListeners(Class<? extends Annotation> annotation) {
        if(listeners.containsKey(annotation)){
            return listeners.get(annotation);
        }

        return Collections.emptySet();
    }

    public boolean contains(Class<? extends Annotation> annotation) {
        return listeners.containsKey(annotation);
    }
}
