package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Observes;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.WeakObserver;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ObservesAnalysis extends ASTAnalysisAdaptor {

    private InjectionPointFactory injectionNodeFactory;
    private Analyzer analyzer;
    private ASTClassFactory astClassFactory;

    @Inject
    public ObservesAnalysis(InjectionPointFactory injectionNodeFactory, Analyzer analyzer, ASTClassFactory astClassFactory) {
        this.injectionNodeFactory = injectionNodeFactory;
        this.analyzer = analyzer;
        this.astClassFactory = astClassFactory;
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
                ASTType weakObserverASTType = astClassFactory.buildASTClassType(WeakObserver.class);
                InjectionNode weakObserverInjectionNode = analyzer.analyze(weakObserverASTType, weakObserverASTType, context);
                injectionNode.addAspect(new ObservesAspect(eventManagerInjectionNode, weakObserverInjectionNode));
            }
            ObservesAspect aspect = injectionNode.getAspect(ObservesAspect.class);

            aspect.addObserver(firstParameter.getASTType(), astMethod);
        }
    }
}
