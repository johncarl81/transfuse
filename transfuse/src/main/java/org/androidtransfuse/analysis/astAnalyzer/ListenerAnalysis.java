package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.model.InjectionNode;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Analyzes the given class for listener annotations.  Addes these annotated methods to a ListenerAspect for
 * code generation during the generation phase.
 *
 * @author John Ericksen
 */
public class ListenerAnalysis extends ASTAnalysisAdaptor {

    private final Map<Class<?>, String> methodAnnotations;

    public ListenerAnalysis() {
        Map<Class<?>, String> eventListenerAnnotations = new HashMap<Class<?>, String>();
        eventListenerAnnotations.put(OnCreate.class, "onCreate");
        eventListenerAnnotations.put(OnDestroy.class, "onDestroy");
        eventListenerAnnotations.put(OnPause.class, "onPause");
        eventListenerAnnotations.put(OnRestart.class, "onRestart");
        eventListenerAnnotations.put(OnResume.class, "onResume");
        eventListenerAnnotations.put(OnStart.class, "onStart");
        eventListenerAnnotations.put(OnStop.class, "onStop");
        eventListenerAnnotations.put(OnLowMemory.class, "onLowMemory");
        eventListenerAnnotations.put(OnSaveInstanceState.class, "onSaveInstanceState");
        eventListenerAnnotations.put(OnRestoreInstanceState.class, "onRestoreInstanceState");
        eventListenerAnnotations.put(OnBackPressed.class, "onBackPressed");
        //application
        eventListenerAnnotations.put(OnTerminate.class, "onTerminate");
        eventListenerAnnotations.put(OnConfigurationChanged.class, "onConfigurationChanged");
        //List Activity
        eventListenerAnnotations.put(OnListItemClick.class, "onListItemClick");
        //BroadcastReceiver
        eventListenerAnnotations.put(OnReceive.class, "onReceive");
        //Service
        eventListenerAnnotations.put(OnRebind.class, "onRebind");
        //Fragment
        eventListenerAnnotations.put(OnActivityCreated.class, "onActivityCreated");
        eventListenerAnnotations.put(OnDestroyView.class, "onDestroyView");
        eventListenerAnnotations.put(OnDetach.class, "onDetach");

        this.methodAnnotations = Collections.unmodifiableMap(eventListenerAnnotations);
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {
        for (Class<?> annotation : methodAnnotations.keySet()) {
            if (astMethod.isAnnotated((Class<Annotation>) annotation)) {
                addMethod(injectionNode, annotation, astMethod);
            }
        }
    }

    private void addMethod(InjectionNode injectionNode, Class<?> annotation, ASTMethod astMethod) {

        if (!injectionNode.containsAspect(ListenerAspect.class)) {
            injectionNode.addAspect(new ListenerAspect());
        }
        ListenerAspect methodCallbackToken = injectionNode.getAspect(ListenerAspect.class);

        methodCallbackToken.addMethodCallback(methodAnnotations.get(annotation), astMethod);

        if (!annotation.equals(OnCreate.class)) {
            ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);

            if (injectionAspect == null) {
                injectionAspect = new ASTInjectionAspect();
                injectionNode.addAspect(ASTInjectionAspect.class, injectionAspect);
            }

            //injection node is now required outside of the local scope
            injectionAspect.setAssignmentType(ASTInjectionAspect.InjectionAssignmentType.FIELD);
        }

    }
}
