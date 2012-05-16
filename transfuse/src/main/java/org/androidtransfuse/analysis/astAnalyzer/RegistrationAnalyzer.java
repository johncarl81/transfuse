package org.androidtransfuse.analysis.astAnalyzer;

import android.app.Activity;
import android.view.View;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTType;
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
public class RegistrationAnalyzer extends ASTAnalysisAdaptor {

    private Map<ASTType, String> listenerMethods = new HashMap<ASTType, String>();
    private ASTClassFactory astClassFactory;
    private Analyzer analyzer;
    private JCodeModel codeModel;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private InjectionPointFactory injectionPointFactory;

    @Inject
    public RegistrationAnalyzer(Analyzer analyzer, ASTClassFactory astClassFactory, JCodeModel codeModel, VariableInjectionBuilderFactory variableInjectionBuilderFactory, InjectionPointFactory injectionPointFactory) {
        this.analyzer = analyzer;
        this.astClassFactory = astClassFactory;
        this.codeModel = codeModel;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnClickListener.class), "setOnClickListener");
        listenerMethods.put(astClassFactory.buildASTClassType(View.OnClickListener.class), "setOnLongClickListener");
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTField astField, AnalysisContext context) {
        if (astField.isAnnotated(RegisterListener.class)) {
            ASTAnnotation registerAnnotation = getAnnotation(RegisterListener.class, astField.getAnnotations());

            Integer value = registerAnnotation.getProperty("value", Integer.class);
            //todo:take && between injectionNode's listener interfaces and aspects
            Class[] interfaces = registerAnnotation.getProperty("interfaces", Class[].class);

            List<String> methods = new ArrayList<String>();

            ASTType viewAstType = astClassFactory.buildASTClassType(View.class);
            InjectionNode viewInjectionNode = analyzer.analyze(viewAstType, viewAstType, context);

            ASTType activityType = astClassFactory.buildASTClassType(Activity.class);
            InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(activityType, context);

            try {

                viewInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildViewVariableBuilder(value, activityInjectionNode, codeModel.parseType(viewAstType.getName())));

                for (Map.Entry<ASTType, String> classStringEntry : listenerMethods.entrySet()) {
                    if (inheritsFrom(astField.getASTType(), classStringEntry.getKey())) {
                        methods.add(classStringEntry.getValue());
                    }
                }

                if (!methods.isEmpty()) {
                    injectionNode.addAspect(new RegistrationAspect(viewInjectionNode, astField, methods));
                }

            } catch (ClassNotFoundException e) {
                throw new TransfuseAnalysisException("Unable to parse type " + viewAstType.getName(), e);
            }

        }
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
