package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.module.ModuleProcessor;
import org.androidtransfuse.annotations.TransfuseModule;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class TransfuseProcessor {

    private ModuleProcessor moduleProcessor;
    private Provider<ApplicationProcessor> applicationProcessorProvider;

    @Inject
    public TransfuseProcessor(ModuleProcessor moduleProcessor,
                              Provider<ApplicationProcessor> applicationProcessorProvider) {
        this.moduleProcessor = moduleProcessor;
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

    public ApplicationProcessor getApplicationProcessor() {
        return applicationProcessorProvider.get();
    }
}
