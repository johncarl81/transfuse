package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.annotations.Observes;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ObservesAnalysis extends ASTAnalysisAdaptor {

    private InjectionPointFactory injectionNodeFactory;

    @Inject
    public ObservesAnalysis(InjectionPointFactory injectionNodeFactory) {
        this.injectionNodeFactory = injectionNodeFactory;
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context) {

        ASTParameter firstParameter = null;
        if(astMethod.getParameters().size() > 0){
            firstParameter = astMethod.getParameters().get(0);
        }
        for(int i = 1; i < astMethod.getParameters().size(); i++){
            if(astMethod.getParameters().get(i).isAnnotated(Observes.class)){
                throw new TransfuseAnalysisException("Malformed event Observer found on " + astMethod.getName());
            }
        }

        if(firstParameter != null && (firstParameter.isAnnotated(Observes.class) || astMethod.isAnnotated(Observes.class))){

            if(!injectionNode.containsAspect(ObservesAspect.class)){
                InjectionNode eventManagerInjectionNode = injectionNodeFactory.buildInjectionNode(EventManager.class, context);
                injectionNode.addAspect(new ObservesAspect(eventManagerInjectionNode));
            }
            ObservesAspect aspect = injectionNode.getAspect(ObservesAspect.class);

            aspect.addObserver(firstParameter.getASTType(), astMethod);
        }
    }
}
