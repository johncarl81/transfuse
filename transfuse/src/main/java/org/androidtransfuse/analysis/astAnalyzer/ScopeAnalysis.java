package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.ScopeAspectFactoryRepository;
import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
 * Analysis to determine if the given type is scoped.
 *
 * @author John Ericksen
 */
public class ScopeAnalysis extends ASTAnalysisAdaptor {

    private final ScopeAspectFactoryRepository scopeAspectFactoryRepository;

    @Inject
    public ScopeAnalysis(ScopeAspectFactoryRepository scopeAspectFactoryRepository) {
        this.scopeAspectFactoryRepository = scopeAspectFactoryRepository;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType concreteType, AnalysisContext context) {

        if (injectionNode.getASTType().equals(concreteType)) {
            for (Class<? extends Annotation> scopeType : scopeAspectFactoryRepository.getScopes()) {
                if (concreteType.isAnnotated(scopeType)) {
                    ScopeAspectFactory scopeAspectFactory = scopeAspectFactoryRepository.getScopeAspectFactory(scopeType);
                    injectionNode.addAspect(scopeAspectFactory.buildAspect(injectionNode, concreteType, context));
                }
            }
        }
    }
}
