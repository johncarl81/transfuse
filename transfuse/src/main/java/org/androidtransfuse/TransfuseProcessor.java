package org.androidtransfuse;

import org.androidtransfuse.analysis.ModuleProcessor;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.processor.ApplicationProcessor;
import org.androidtransfuse.processor.ProcessorContext;
import org.androidtransfuse.processor.ProcessorFactory;

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

    private Manifest manifest = null;
    private RResource rResource = null;

    @Inject
    public TransfuseProcessor(ModuleProcessor moduleProcessor,
                              ProcessorFactory processorFactory) {
        this.moduleProcessor = moduleProcessor;
        this.processorFactory = processorFactory;
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

    public void processManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public void processR(RResource rResource) {
        this.rResource = rResource;
    }

    public ApplicationProcessor getApplicationProcessor() {
        ProcessorContext processorContext = processorFactory.buildContext(rResource, manifest, moduleProcessor);
        return processorFactory.buildApplicationProcessor(processorContext);
    }
}
