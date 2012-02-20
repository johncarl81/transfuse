package org.androidtransfuse.gen.scopeBuilder;

import android.app.Application;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SingletonScopeAspectFactory implements ScopeAspectFactory {

    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;
    private ScopeBuilderFactory scopeBuilderFactory;

    @Inject
    public SingletonScopeAspectFactory(ASTClassFactory astClassFactory, InjectionPointFactory injectionPointFactory, ScopeBuilderFactory scopeBuilderFactory) {
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.scopeBuilderFactory = scopeBuilderFactory;
    }

    @Override
    public ScopeAspect buildAspect(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        ASTType applicationType = astClassFactory.buildASTClassType(Application.class);
        InjectionNode applicationInjectionNode = injectionPointFactory.buildInjectionNode(applicationType, context);

        return new ScopeAspect(scopeBuilderFactory.buildSingletonScopeBuilder(applicationInjectionNode));
    }
}
