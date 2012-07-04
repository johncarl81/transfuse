package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ObservesAspect {

    private Map<ASTType, Set<MethodSignatureWrapper>> observesMap = new HashMap<ASTType, Set<MethodSignatureWrapper>>();
    private InjectionNode eventManagerInjectionNode;
    private InjectionNode observerTendingInjectionNode;

    public ObservesAspect(InjectionNode eventManagerInjectionNode, InjectionNode observerTendingInjectionNode) {
        this.eventManagerInjectionNode = eventManagerInjectionNode;
        this.observerTendingInjectionNode = observerTendingInjectionNode;
    }

    public void addObserver(ASTType event, ASTMethod method){
        if(!observesMap.containsKey(event)){
            observesMap.put(event, new HashSet<MethodSignatureWrapper>());
        }

        observesMap.get(event).add(new MethodSignatureWrapper(method));
    }

    public Set<ASTType> getEvents(){
        return observesMap.keySet();
    }

    public Set<ASTMethod> getObserverMethods(ASTType event){
        Set<ASTMethod> methods = new HashSet<ASTMethod>();

        if(observesMap.containsKey(event)){
            for (MethodSignatureWrapper methodSignatureWrapper : observesMap.get(event)) {
                methods.add(methodSignatureWrapper.getMethod());
            }
        }

        return methods;
    }

    public InjectionNode getEventManagerInjectionNode() {
        return eventManagerInjectionNode;
    }

    public InjectionNode getObserverTendingInjectionNode() {
        return observerTendingInjectionNode;
    }
}
