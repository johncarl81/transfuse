package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTMethodUniqueSignatureDecorator;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import java.util.*;

/**
 * @author John Ericksen
 */
public class ObservesAspect {

    private Map<ASTType, Set<ASTMethod>> observesMap = new HashMap<ASTType, Set<ASTMethod>>();
    private InjectionNode eventManagerInjectionNode;
    private InjectionNode observerTendingInjectionNode;

    public ObservesAspect(InjectionNode eventManagerInjectionNode, InjectionNode observerTendingInjectionNode) {
        this.eventManagerInjectionNode = eventManagerInjectionNode;
        this.observerTendingInjectionNode = observerTendingInjectionNode;
    }

    public void addObserver(ASTType event, ASTMethod method){
        if(!observesMap.containsKey(event)){
            observesMap.put(event, new HashSet<ASTMethod>());
        }

        observesMap.get(event).add(new ASTMethodUniqueSignatureDecorator(method));
    }

    public Set<ASTType> getEvents(){
        return observesMap.keySet();
    }

    public Set<ASTMethod> getObserverMethods(ASTType event){
        if(observesMap.containsKey(event)){
            return observesMap.get(event);
        }

        return Collections.emptySet();
    }

    public InjectionNode getEventManagerInjectionNode() {
        return eventManagerInjectionNode;
    }

    public InjectionNode getObserverTendingInjectionNode() {
        return observerTendingInjectionNode;
    }
}
