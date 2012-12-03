package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.module.ImplementedByProcessor;
import org.androidtransfuse.analysis.module.ModuleProcessor;
import org.androidtransfuse.analysis.repository.GeneratorRepository;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.gen.ManifestBuilder;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class TransfuseProcessor {

    private final ModuleProcessor moduleProcessor;
    private final ImplementedByProcessor implementedByProcessor;
    private final GeneratorRepository generatorRepository;
    private final ManifestBuilder manifestBuilder;

    @Inject
    public TransfuseProcessor(ModuleProcessor moduleProcessor,
                              ImplementedByProcessor implementedByProcessor,
                              GeneratorRepository generatorRepository,
                              ManifestBuilder manifestBuilder,
                              ManifestManager manifestManager,
                              Merger merger,
                              @Named(TransfuseGenerateGuiceModule.ORIGINAL_MANIFEST) Manifest originalManifest,
                              Logger logger) {
        this.moduleProcessor = moduleProcessor;
        this.implementedByProcessor = implementedByProcessor;
        this.generatorRepository = generatorRepository;
        this.manifestBuilder = manifestBuilder;
    }

    public void processModule(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {
            if (astType.isAnnotated(TransfuseModule.class)) {
                for (ASTMethod astMethod : astType.getMethods()) {
                    moduleProcessor.processMethod(astMethod);
                }
            }
        }
    }

    public void processImplementedBy(Collection<? extends ASTType> astTypes) {
        for (ASTType astType : astTypes) {
            implementedByProcessor.processType(astType);
        }
    }

    public void generateEmptyApplication() {
        manifestBuilder.setupManifestApplication(android.app.Application.class.getName());
    }

    public void submit(Class<? extends Annotation> componentAnnotation, Collection<Provider<ASTType>> astProviders) {
        for (Provider<ASTType> astProvider : astProviders) {
            TransactionProcessorBuilder builder = generatorRepository.getComponentBuilder(componentAnnotation);
            if (builder == null) {
                throw new TransfuseAnalysisException("Builder for type " + componentAnnotation.getName() + " not found");
            }

            builder.submit(astProvider);
        }
    }

    public void execute() {
        generatorRepository.getProcessor().execute();
    }

    public void getError() {
        for (TransactionProcessorBuilder transactionProcessor : generatorRepository.getComponentBuilders().values()) {
            if (!transactionProcessor.getTransactionProcessor().isComplete()) {
                Exception error = transactionProcessor.getTransactionProcessor().getError();
                throw new TransfuseAnalysisException("Code generation did not complete successfully.", error);
            }
        }
    }
}
