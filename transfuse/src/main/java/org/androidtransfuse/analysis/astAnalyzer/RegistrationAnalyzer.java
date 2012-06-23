package org.androidtransfuse.analysis.astAnalyzer;

import android.app.Activity;
import android.view.View;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.*;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
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
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnLongClickListener.class), "setOnLongClickListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnCreateContextMenuListener.class), "setOnCreateContextMenuListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnKeyListener.class), "setOnKeyListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnTouchListener.class), "setOnTouchListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnFocusChangeListener.class), "setOnFocusChangeListener");
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, final ASTType astType, AnalysisContext context) {
        analyze(astType, astType, injectionNode, context, new TypeListenerRegistrationFactory(astType));
    }

    private static final class TypeListenerRegistrationFactory implements ListenerRegistrationFactory<Object> {

        private ASTType astType;

        private TypeListenerRegistrationFactory(ASTType astType) {
            this.astType = astType;
        }

        @Override
        public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<String> methods) {

            registrationAspect.addTypeRegistration(new ListenerRegistration<ASTType>(viewInjectionNode, methods, astType));
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, final ASTMethod astMethod, AnalysisContext context) {
        analyze(astMethod, astMethod.getReturnType(), injectionNode, context, new MethodListenerRegistrationFactory(astMethod));
    }

    private static final class MethodListenerRegistrationFactory implements ListenerRegistrationFactory<Object> {

        private ASTMethod astMethod;

        private MethodListenerRegistrationFactory(ASTMethod astMethod) {
            this.astMethod = astMethod;
        }

        @Override
        public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<String> methods) {

            registrationAspect.addMethodRegistration(new ListenerRegistration<ASTMethod>(viewInjectionNode, methods, astMethod));
        }
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, final ASTField astField, AnalysisContext context) {
        analyze(astField, astField.getASTType(), injectionNode, context, new FieldListenerRegistrationFactory(astField));
    }

    private static final class FieldListenerRegistrationFactory implements ListenerRegistrationFactory<Object> {

        private ASTField astField;

        private FieldListenerRegistrationFactory(ASTField astField) {
            this.astField = astField;
        }

        @Override
        public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<String> methods) {

            registrationAspect.addFieldRegistration(new ListenerRegistration<ASTField>(viewInjectionNode, methods, astField));
        }
    }

    private <T> void analyze(ASTBase astBase, ASTType astType, InjectionNode injectionNode, AnalysisContext context, ListenerRegistrationFactory<T> factory) {
        if (astBase.isAnnotated(RegisterListener.class)) {
            ASTAnnotation registerAnnotation = astBase.getASTAnnotation(RegisterListener.class);

            Integer viewId = registerAnnotation.getProperty("value", Integer.class);
            String viewTag = registerAnnotation.getProperty("tag", String.class);
            ASTType[] interfaces = registerAnnotation.getProperty("interfaces", ASTType[].class);

            List<ASTType> interfaceList = new ArrayList<ASTType>();
            if (interfaces != null) {
                interfaceList.addAll(Arrays.asList(interfaces));
            }

            InjectionNode viewInjectionNode = buildViewInjectionNode(viewId, viewTag, context);

            List<String> methods = buildListenerMethods(astType, interfaceList);


            if (!methods.isEmpty()) {
                RegistrationAspect registrationAspect = getRegistrationAspect(injectionNode);

                factory.assignRegistration(registrationAspect, viewInjectionNode, methods);
            }
        }
    }

    private interface ListenerRegistrationFactory<T> {

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

    private InjectionNode buildViewInjectionNode(Integer viewId, String viewTag, AnalysisContext context) {

        InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(Activity.class, context);

        ASTType viewAstType = astClassFactory.buildASTClassType(View.class);
        InjectionNode viewInjectionNode = analyzer.analyze(viewAstType, viewAstType, context);

        try {
            viewInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildViewVariableBuilder(viewId, viewTag, activityInjectionNode, codeModel.parseType(viewAstType.getName())));

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
}