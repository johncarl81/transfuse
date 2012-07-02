package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.model.InjectionNode;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class MethodCallbackAnalysis extends ASTAnalysisAdaptor {

    private Map<Class<?>, String> methodAnnotations = new HashMap<Class<?>, String>();

    public MethodCallbackAnalysis() {
        methodAnnotations.put(OnCreate.class, "onCreate");
        methodAnnotations.put(OnTouch.class, "onTouch");
        methodAnnotations.put(OnDestroy.class, "onDestroy");
        methodAnnotations.put(OnPause.class, "onPause");
        methodAnnotations.put(OnRestart.class, "onRestart");
        methodAnnotations.put(OnResume.class, "onResume");
        methodAnnotations.put(OnStart.class, "onStart");
        methodAnnotations.put(OnStop.class, "onStop");
        methodAnnotations.put(OnLowMemory.class, "onLowMemory");
        //application
        methodAnnotations.put(OnTerminate.class, "onTerminate");
        methodAnnotations.put(OnConfigurationChanged.class, "onConfigurationChanged");
        //List Activity
        methodAnnotations.put(OnListItemClick.class, "onListItemClick");
        //BroadcastReceiver
        methodAnnotations.put(OnReceive.class, "onReceive");
        //Service
        methodAnnotations.put(OnRebind.class, "onRebind");
        //Fragment
        methodAnnotations.put(OnActivityCreated.class, "onActivityCreated");
        methodAnnotations.put(OnDestroyView.class, "onDestroyView");
        methodAnnotations.put(OnDetach.class, "onDetach");
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context) {
        for (Class<?> annotation : methodAnnotations.keySet()) {
            if (astMethod.isAnnotated((Class<Annotation>) annotation)) {
                addMethod(injectionNode, annotation, astMethod);
            }
        }
    }

    private void addMethod(InjectionNode injectionNode, Class<?> annotation, ASTMethod astMethod) {

        if (!injectionNode.containsAspect(MethodCallbackAspect.class)) {
            injectionNode.addAspect(new MethodCallbackAspect());
        }
        MethodCallbackAspect methodCallbackToken = injectionNode.getAspect(MethodCallbackAspect.class);

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
