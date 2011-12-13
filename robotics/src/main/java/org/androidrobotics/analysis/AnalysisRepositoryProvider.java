package org.androidrobotics.analysis;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class AnalysisRepositoryProvider implements Provider<AnalysisRepository> {

    AnalysisRepository analysisRepository = new AnalysisRepository();

    public void setAnalysisRepository(AnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    @Override
    public AnalysisRepository get() {
        return analysisRepository;
    }
}
