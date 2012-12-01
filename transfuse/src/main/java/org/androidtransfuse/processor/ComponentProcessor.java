package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.GeneratorRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ComponentProcessor {

    private final GeneratorRepository generatorRepository;

    @Inject
    public ComponentProcessor(GeneratorRepository generatorRepository) {
        this.generatorRepository = generatorRepository;
    }

    public void submit(Class<? extends Annotation> componentAnnotation, Collection<Provider<ASTType>> astProviders) {
        for (Provider<ASTType> astProvider : astProviders) {
            TransactionProcessorBuilder builder = generatorRepository.getBuilder(componentAnnotation);
            if (builder == null) {
                throw new TransfuseAnalysisException("Builder for type " + componentAnnotation.getName() + " not found");
            }

            builder.submit(astProvider);
        }
    }

    public void execute() {
        for (TransactionProcessorBuilder transactionProcessor : generatorRepository.getRepository().values()) {
            transactionProcessor.getTransactionProcessor().execute();
        }
    }

    public void getError() {
        for (TransactionProcessorBuilder transactionProcessor : generatorRepository.getRepository().values()) {
            if (!transactionProcessor.getTransactionProcessor().isComplete()) {
                Exception error = transactionProcessor.getTransactionProcessor().getError();
                throw new TransfuseAnalysisException("Code generation did not complete successfully.", error);
            }
        }
    }
}
