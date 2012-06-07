package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;

/**
 * @author John Ericksen
 */
public interface AnalysisContextFactory {

    AnalysisContext buildAnalysisContext(InjectionNodeBuilderRepository injectionNodeBuilderRepository);
}
