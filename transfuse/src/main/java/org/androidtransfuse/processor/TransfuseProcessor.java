package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.module.ModuleProcessor;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class TransfuseProcessor {

    private ModuleProcessor moduleProcessor;
    private ProcessorFactory processorFactory;

    private Manifest manifest;
    private RResource rResource;

    @Inject
    public TransfuseProcessor(@Assisted Manifest manifest,
                              @Assisted RResource rResource,
                              ModuleProcessor moduleProcessor,
                              ProcessorFactory processorFactory) {
        this.moduleProcessor = moduleProcessor;
        this.processorFactory = processorFactory;
        this.manifest = manifest;
        this.rResource = rResource;
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
        ProcessorContext processorContext = processorFactory.buildContext(rResource, manifest);
        return processorFactory.buildApplicationProcessor(processorContext);
    }
}
