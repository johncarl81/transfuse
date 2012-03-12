package org.androidtransfuse.analysis;

import org.androidtransfuse.gen.InjectionNodeBuilderRepository;

/**
 * @author John Ericksen
 */
public interface AnalysisContextFactory {

    AnalysisContext buildAnalysisContext(AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilderRepository);
}
