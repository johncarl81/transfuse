package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public interface AnalysisGenerationTransactionProcessorBuilderFactory {

    AnalysisGenerationTransactionProcessorBuilder buildBuilder(WorkerProvider workerProvider);
}
