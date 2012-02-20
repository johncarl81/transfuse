package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
 * @author John Ericksen
 */
public class ScopeAnalysis extends ASTAnalysisAdaptor {

    private ScopeAspectFactoryRepository scopeAspectFactoryRepository;

    @Inject
    public ScopeAnalysis(ScopeAspectFactoryRepository scopeAspectFactoryRepository) {
        this.scopeAspectFactoryRepository = scopeAspectFactoryRepository;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        if (context.getSuperClassLevel() == 0) {

            for (Class<? extends Annotation> scopeType : scopeAspectFactoryRepository.getScopes()) {
                if (astType.isAnnotated(scopeType)) {
                    ScopeAspectFactory scopeAspectFactory = scopeAspectFactoryRepository.getScopeAspectFactory(scopeType);
                    injectionNode.addAspect(scopeAspectFactory.buildAspect(injectionNode, astType, context));
                }
            }
        }
    }
}
