package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.ASTAnalysis;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTConstructor;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectionAnalyzer implements ASTAnalysis {

    private InjectionPointFactory injectionPointFactory;

    @Inject
    public InjectionAnalyzer(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {

        if (context.getSuperClassLevel() == 0) {
            //only analyze the root class for constructor injection
            ASTConstructor noArgConstructor = null;
            ASTConstructor annotatedConstructor = null;

            for (ASTConstructor astConstructor : astType.getConstructors()) {
                if (astConstructor.isAnnotated(Inject.class)) {
                    annotatedConstructor = astConstructor;
                    getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(astConstructor, context));
                }
                if (astConstructor.getParameters().size() == 0) {
                    noArgConstructor = astConstructor;
                }
            }

            if (annotatedConstructor != null) {
                getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(annotatedConstructor, context));
            } else if (noArgConstructor != null) {
                getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(noArgConstructor, context));
            } else {
                throw new TransfuseAnalysisException("No-Arg Constructor required for injection point: " + injectionNode.getClassName());
            }
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context) {
        if (astMethod.isAnnotated(Inject.class)) {
            getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(astMethod, context));
        }
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTField astField, AnalysisContext context) {
        if (astField.isAnnotated(Inject.class)) {
            getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(astField, context));
        }
    }

    private ASTInjectionAspect getInjectionToken(InjectionNode injectionNode) {
        if (!injectionNode.containsAspect(ASTInjectionAspect.class)) {
            injectionNode.addAspect(new ASTInjectionAspect());
        }
        return injectionNode.getAspect(ASTInjectionAspect.class);
    }
}
