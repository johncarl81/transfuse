package org.androidtransfuse.gen;

import org.androidtransfuse.analysis.Analysis;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class AnalysisGeneration<T> implements Generator<ASTType> {

    private Analysis<T> analysis;
    private Generator<T> generator;

    public AnalysisGeneration(Analysis<T> analysis,
                              Generator<T> generator) {
        this.analysis = analysis;
        this.generator = generator;
    }

    @Override
    public void generate(ASTType astType) {
        T descriptor = analysis.analyze(astType);

        if (descriptor != null) {
            generator.generate(descriptor);
        }
    }
}
