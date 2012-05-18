package org.androidtransfuse.analysis.astAnalyzer;

import android.app.Activity;
import android.view.View;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.*;
import org.androidtransfuse.analysis.adapter.*;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author John Ericksen
 */
public class RegistrationAnalyzer implements ASTAnalysis {

    private Map<ASTType, String> listenerMethods = new HashMap<ASTType, String>();
    private ASTClassFactory astClassFactory;
    private Analyzer analyzer;
    private JCodeModel codeModel;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private InjectionPointFactory injectionPointFactory;

    @Inject
    public RegistrationAnalyzer(Analyzer analyzer,
                                ASTClassFactory astClassFactory,
                                JCodeModel codeModel,
                                VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                InjectionPointFactory injectionPointFactory) {
        this.analyzer = analyzer;
        this.astClassFactory = astClassFactory;
        this.codeModel = codeModel;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnClickListener.class), "setOnClickListener");
        //listenerMethods.put(astClassFactory.buildASTClassType(View.OnLongClickListener.class), "setOnLongClickListener");
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        if (astType.isAnnotated(RegisterListener.class)) {

            ASTAnnotation registerAnnotation = getAnnotation(RegisterListener.class, astType.getAnnotations());

            Integer viewId = registerAnnotation.getProperty("value", Integer.class);
            //todo:take && between injectionNode's listener interfaces and aspects
            //Class[] interfaces = registerAnnotation.getProperty("interfaces", Class[].class);

            InjectionNode viewInjectionNode = buildViewInjectionNode(viewId, context);

            List<String> methods = buildListenerMethods(astType, astType.getAnnotations(), new ArrayList<ASTType>());


            if (!methods.isEmpty()) {
                RegistrationAspect registrationAspect = getRegistrationAspect(injectionNode);

                registrationAspect.addTypeRegistration(new ListenerRegistration<ASTType>(viewInjectionNode, methods, astType));
            }
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context) {
        if (astMethod.isAnnotated(RegisterListener.class)) {
            ASTAnnotation registerAnnotation = getAnnotation(RegisterListener.class, astMethod.getAnnotations());

            Integer viewId = registerAnnotation.getProperty("value", Integer.class);
            //todo:take && between injectionNode's listener interfaces and aspects
            //Class[] interfaces = registerAnnotation.getProperty("interfaces", Class[].class);

            InjectionNode viewInjectionNode = buildViewInjectionNode(viewId, context);

            List<String> methods = buildListenerMethods(astMethod.getReturnType(), astMethod.getAnnotations(), new ArrayList<ASTType>());


            if (!methods.isEmpty()) {
                RegistrationAspect registrationAspect = getRegistrationAspect(injectionNode);

                registrationAspect.addMethodRegistration(new ListenerRegistration<ASTMethod>(viewInjectionNode, methods, astMethod));
            }
        }
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTField astField, AnalysisContext context) {
        if (astField.isAnnotated(RegisterListener.class)) {
            ASTAnnotation registerAnnotation = getAnnotation(RegisterListener.class, astField.getAnnotations());

            Integer viewId = registerAnnotation.getProperty("value", Integer.class);
            //todo:take && between injectionNode's listener interfaces and aspects
            //Class[] interfaces = registerAnnotation.getProperty("interfaces", Class[].class);

            InjectionNode viewInjectionNode = buildViewInjectionNode(viewId, context);

            List<String> methods = buildListenerMethods(astField.getASTType(), astField.getAnnotations(), new ArrayList<ASTType>());


            if (!methods.isEmpty()) {
                RegistrationAspect registrationAspect = getRegistrationAspect(injectionNode);

                registrationAspect.addFieldRegistration(new ListenerRegistration<ASTField>(viewInjectionNode, methods, astField));
            }
        }
    }

    private List<String> buildListenerMethods(ASTType astType, Collection<ASTAnnotation> annotations, List<ASTType> interfaceList) {

        List<String> methods = new ArrayList<String>();


        for (Map.Entry<ASTType, String> classStringEntry : listenerMethods.entrySet()) {
            if (inheritsFrom(astType, classStringEntry.getKey()) && (interfaceList.isEmpty() || interfaceList.contains(astType))) {
                methods.add(classStringEntry.getValue());
            }
        }

        return methods;
    }

    private InjectionNode buildViewInjectionNode(Integer viewId, AnalysisContext context) {

        ASTType activityType = astClassFactory.buildASTClassType(Activity.class);
        InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(activityType, context);

        ASTType viewAstType = astClassFactory.buildASTClassType(View.class);
        InjectionNode viewInjectionNode = analyzer.analyze(viewAstType, viewAstType, context);

        try {
            viewInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildViewVariableBuilder(viewId, activityInjectionNode, codeModel.parseType(viewAstType.getName())));

            return viewInjectionNode;

        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse type " + viewAstType.getName(), e);
        }
    }

    private RegistrationAspect getRegistrationAspect(InjectionNode injectionNode) {
        if (!injectionNode.containsAspect(RegistrationAspect.class)) {
            injectionNode.addAspect(new RegistrationAspect());
        }
        return injectionNode.getAspect(RegistrationAspect.class);
    }

    private boolean inheritsFrom(ASTType astType, ASTType inheritable) {
        if (astType == null) {
            return false;
        }
        if (astType.equals(inheritable)) {
            return true;
        }
        for (ASTType typeInterfaces : astType.getInterfaces()) {
            if (inheritsFrom(typeInterfaces, inheritable)) {
                return true;
            }
        }
        return inheritsFrom(astType.getSuperClass(), inheritable);
    }

    //todo:move this (and duplicate) to shared utility class or method on ASTBase?
    private ASTAnnotation getAnnotation(Class<? extends Annotation> resourceClass, Collection<ASTAnnotation> annotations) {
        ASTAnnotation annotation = null;

        for (ASTAnnotation astAnnotation : annotations) {
            if (astAnnotation.getName().equals(resourceClass.getName())) {
                annotation = astAnnotation;
            }
        }

        if (annotation == null) {
            throw new TransfuseAnalysisException("Unable to find annotation: " + resourceClass.getName());
        }

        return annotation;
    }
}