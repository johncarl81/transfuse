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
import org.androidtransfuse.event.EventTending;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * Analysis class to find the methods annotated with @Observes.  When found, an ObservesAspect is populated with the
 * observing method.  Multiple observer methods could be defined per class and per InjectionNode
 *
 * @author John Ericksen
 */
public class ObservesAnalysis extends ASTAnalysisAdaptor {

    private final InjectionPointFactory injectionNodeFactory;
    private final Analyzer analyzer;
    private final ASTClassFactory astClassFactory;

    @Inject
    public ObservesAnalysis(InjectionPointFactory injectionNodeFactory, Analyzer analyzer, ASTClassFactory astClassFactory) {
        this.injectionNodeFactory = injectionNodeFactory;
        this.analyzer = analyzer;
        this.astClassFactory = astClassFactory;
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {

        ASTParameter firstParameter = null;
        if(astMethod.getParameters().size() > 0){
            firstParameter = astMethod.getParameters().get(0);
        }
        for(int i = 1; i < astMethod.getParameters().size(); i++){
            if(astMethod.getParameters().get(i).isAnnotated(Observes.class)){
                //don't accept @Observes outside of the first parameter
                throw new TransfuseAnalysisException("Malformed event Observer found on " + astMethod.getName());
            }
        }

        if(firstParameter != null && (firstParameter.isAnnotated(Observes.class) || astMethod.isAnnotated(Observes.class))){
            //don't accept @Observes with more than one parameter
            if(astMethod.getParameters().size() != 1){
                throw new TransfuseAnalysisException("Malformed event Observer found on " + astMethod.getName());
            }

            if(!injectionNode.containsAspect(ObservesAspect.class)){
                InjectionNode eventManagerInjectionNode = injectionNodeFactory.buildInjectionNode(EventManager.class, context);
                ASTType observerTestingASType = astClassFactory.buildASTClassType(EventTending.class);
                InjectionNode observerTendingInjectionNode = analyzer.analyze(observerTestingASType, observerTestingASType, context);
                injectionNode.addAspect(new ObservesAspect(eventManagerInjectionNode, observerTendingInjectionNode));
            }
            ObservesAspect aspect = injectionNode.getAspect(ObservesAspect.class);

            aspect.addObserver(firstParameter.getASTType(), astMethod);
        }
    }
}
