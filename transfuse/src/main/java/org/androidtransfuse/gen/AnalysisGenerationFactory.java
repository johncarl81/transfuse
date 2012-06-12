package org.androidtransfuse.gen;

import org.androidtransfuse.analysis.Analysis;

/**
 * @author John Ericksen
 */
public class AnalysisGenerationFactory {

    public <T> AnalysisGeneration<T> buildAnalysisGeneration(Analysis<T> analysis, Generator<T> generator) {
        return new AnalysisGeneration<T>(analysis, generator);
    }
}
