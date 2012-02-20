package org.androidtransfuse.gen.scopeBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface ScopeAspectFactory {

    ScopeAspect buildAspect(InjectionNode injectionNode, ASTType astType, AnalysisContext context);

}
