package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.module.ImplementedByProcessor;
import org.androidtransfuse.analysis.module.ModuleProcessor;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.gen.ApplicationGenerator;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class TransfuseProcessor {

    private final ModuleProcessor moduleProcessor;
    private final ImplementedByProcessor implementedByProcessor;
    private final Provider<ApplicationGenerator> applicationProcessorProvider;

    @Inject
    public TransfuseProcessor(ModuleProcessor moduleProcessor,
                              ImplementedByProcessor implementedByProcessor,
                              Provider<ApplicationGenerator> applicationProcessorProvider) {
        this.moduleProcessor = moduleProcessor;
        this.implementedByProcessor = implementedByProcessor;
        this.applicationProcessorProvider = applicationProcessorProvider;
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

    public ApplicationGenerator getApplicationProcessor() {
        return applicationProcessorProvider.get();
    }

    public void processImplementedBy(Collection<? extends ASTType> astTypes) {
        for (ASTType astType : astTypes) {
            implementedByProcessor.processType(astType);
        }
    }
}
