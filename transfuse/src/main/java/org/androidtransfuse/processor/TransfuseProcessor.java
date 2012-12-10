package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ManifestBuilder;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class TransfuseProcessor {

    private final GeneratorRepository generatorRepository;
    private final ManifestBuilder manifestBuilder;

    @Inject
    public TransfuseProcessor(GeneratorRepository generatorRepository,
                              ManifestBuilder manifestBuilder) {
        this.generatorRepository = generatorRepository;
        this.manifestBuilder = manifestBuilder;
    }

    public void generateEmptyApplication() {
        manifestBuilder.setupManifestApplication(android.app.Application.class.getName());
    }

    public void submit(Class<? extends Annotation> componentAnnotation, Collection<Provider<ASTType>> astProviders) {
        for (Provider<ASTType> astProvider : astProviders) {
            TransactionProcessorBuilder<Provider<ASTType>, ?> builder = generatorRepository.getComponentBuilder(componentAnnotation);
            if (builder == null) {
                throw new TransfuseAnalysisException("Builder for type " + componentAnnotation.getName() + " not found");
            }

            builder.submit(astProvider);
        }
    }

    public void execute() {
        generatorRepository.getProcessor().execute();
    }

    public void checkForErrors() {
        boolean errored = false;
        ImmutableSet.Builder<Exception> exceptions = ImmutableSet.builder();

        for (TransactionProcessorBuilder transactionProcessor : generatorRepository.getComponentBuilders().values()) {
            if (!transactionProcessor.getTransactionProcessor().isComplete()) {
                errored = true;
                exceptions.addAll(transactionProcessor.getTransactionProcessor().getErrors());
            }
        }

        if (errored) {
            throw new TransfuseAnalysisException("Code generation did not complete successfully.", exceptions.build());
        }
    }
}
