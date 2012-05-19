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
    private ElementConverterFactory elementConverterFactory;

    @Inject
    public RegistrationAnalyzer(Analyzer analyzer,
                                ASTClassFactory astClassFactory,
                                JCodeModel codeModel,
                                VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                InjectionPointFactory injectionPointFactory, ElementConverterFactory elementConverterFactory) {
        this.analyzer = analyzer;
        this.astClassFactory = astClassFactory;
        this.codeModel = codeModel;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.elementConverterFactory = elementConverterFactory;
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnClickListener.class), "setOnClickListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnLongClickListener.class), "setOnLongClickListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnCreateContextMenuListener.class), "setOnCreateContextMenuListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnKeyListener.class), "setOnKeyListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnTouchListener.class), "setOnTouchListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnFocusChangeListener.class), "setOnFocusChangeListener");
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, final ASTType astType, AnalysisContext context) {
        analyze(astType, astType, injectionNode, context, new ListenerRegistrationFactory<Object>() {
            @Override
            public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<String> methods) {

                registrationAspect.addTypeRegistration(new ListenerRegistration<ASTType>(viewInjectionNode, methods, astType));
            }
        });
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, final ASTMethod astMethod, AnalysisContext context) {
        analyze(astMethod, astMethod.getReturnType(), injectionNode, context, new ListenerRegistrationFactory<Object>() {
            @Override
            public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<String> methods) {

                registrationAspect.addMethodRegistration(new ListenerRegistration<ASTMethod>(viewInjectionNode, methods, astMethod));
            }
        });
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, final ASTField astField, AnalysisContext context) {
        analyze(astField, astField.getASTType(), injectionNode, context, new ListenerRegistrationFactory<Object>() {
            @Override
            public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<String> methods) {

                registrationAspect.addFieldRegistration(new ListenerRegistration<ASTField>(viewInjectionNode, methods, astField));
            }
        });
    }

    private <T> void analyze(ASTBase astBase, ASTType astType, InjectionNode injectionNode, AnalysisContext context, ListenerRegistrationFactory<T> factory) {
        if (astBase.isAnnotated(RegisterListener.class)) {
            ASTAnnotation registerAnnotation = getAnnotation(RegisterListener.class, astBase.getAnnotations());

            Integer viewId = registerAnnotation.getProperty("value", Integer.class);
            ASTType[] interfaces = registerAnnotation.getProperty("interfaces", ASTType[].class);

            List<ASTType> interfaceList = new ArrayList<ASTType>();
            if (interfaces != null) {
                interfaceList.addAll(Arrays.asList(interfaces));
            }

            InjectionNode viewInjectionNode = buildViewInjectionNode(viewId, context);

            List<String> methods = buildListenerMethods(astType, interfaceList);


            if (!methods.isEmpty()) {
                RegistrationAspect registrationAspect = getRegistrationAspect(injectionNode);

                factory.assignRegistration(registrationAspect, viewInjectionNode, methods);
            }
        }
    }

    private static interface ListenerRegistrationFactory<T> {

        void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<String> methods);
    }

    private List<String> buildListenerMethods(ASTType astType, List<ASTType> interfaceList) {

        List<String> methods = new ArrayList<String>();


        for (Map.Entry<ASTType, String> classStringEntry : listenerMethods.entrySet()) {
            if ((interfaceList.isEmpty() || interfaceList.contains(classStringEntry.getKey()))
                    && astType.inheritsFrom(classStringEntry.getKey())) {
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