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

    private Map<ASTType, ASTMethod> listenerMethods = new HashMap<ASTType, ASTMethod>();
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
                                InjectionPointFactory injectionPointFactory) throws NoSuchMethodException {
        this.analyzer = analyzer;
        this.astClassFactory = astClassFactory;
        this.codeModel = codeModel;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        addListenerMethod("setOnClickListener", View.OnClickListener.class);
        addListenerMethod("setOnLongClickListener", View.OnLongClickListener.class);
        addListenerMethod("setOnCreateContextMenuListener", View.OnCreateContextMenuListener.class);
        addListenerMethod("setOnKeyListener", View.OnKeyListener.class);
        addListenerMethod("setOnTouchListener", View.OnTouchListener.class);
        addListenerMethod("setOnFocusChangeListener", View.OnFocusChangeListener.class);
    }

    private void addListenerMethod(String listenerMethod, Class<?> listenerClass) throws NoSuchMethodException {
        ASTType listenerType = astClassFactory.buildASTClassType(listenerClass);

        ASTMethod method = astClassFactory.buildASTClassMethod(View.class.getMethod(listenerMethod, listenerClass));

        listenerMethods.put(listenerType, method);
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
        public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<ASTMethod> methods, int level) {

            registrationAspect.addTypeRegistration(new ListenerRegistration<ASTType>(viewInjectionNode, methods, astType, level));
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, final ASTMethod astMethod, AnalysisContext context) {
        analyze(astMethod, astMethod.getReturnType(), injectionNode, context, new MethodListenerRegistrationFactory(astMethod));
    }

    private static final class MethodListenerRegistrationFactory implements ListenerRegistrationFactory<Object> {

        private ASTMethod astMethod;

        private MethodListenerRegistrationFactory(ASTMethod astMethod) {
            this.astMethod = astMethod;
        }

        @Override
        public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<ASTMethod> methods, int level) {

            registrationAspect.addMethodRegistration(new ListenerRegistration<ASTMethod>(viewInjectionNode, methods, astMethod, level));
        }
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTType concreteType, final ASTField astField, AnalysisContext context) {
        analyze(astField, astField.getASTType(), injectionNode, context, new FieldListenerRegistrationFactory(astField));
    }

    private static final class FieldListenerRegistrationFactory implements ListenerRegistrationFactory<Object> {

        private ASTField astField;

        private FieldListenerRegistrationFactory(ASTField astField) {
            this.astField = astField;
        }

        @Override
        public void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<ASTMethod> methods, int level) {

            registrationAspect.addFieldRegistration(new ListenerRegistration<ASTField>(viewInjectionNode, methods, astField, level));
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

            List<ASTMethod> methods = buildListenerMethods(astType, interfaceList);


            if (!methods.isEmpty()) {
                RegistrationAspect registrationAspect = getRegistrationAspect(injectionNode);

                factory.assignRegistration(registrationAspect, viewInjectionNode, methods, context.getSuperClassLevel());
            }
        }
    }

    private interface ListenerRegistrationFactory<T> {

        void assignRegistration(RegistrationAspect registrationAspect, InjectionNode viewInjectionNode, List<ASTMethod> methods, int level);
    }

    private List<ASTMethod> buildListenerMethods(ASTType astType, List<ASTType> interfaceList) {

        List<ASTMethod> methods = new ArrayList<ASTMethod>();


        for (Map.Entry<ASTType, ASTMethod> classStringEntry : listenerMethods.entrySet()) {
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