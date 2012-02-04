package org.androidrobotics;

import org.androidrobotics.analysis.ModuleProcessor;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.RoboticsModule;
import org.androidrobotics.model.manifest.Manifest;
import org.androidrobotics.model.r.RResource;
import org.androidrobotics.processor.ApplicationProcessor;
import org.androidrobotics.processor.ProcessorContext;
import org.androidrobotics.processor.ProcessorFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class RoboticsProcessor {

    private ModuleProcessor moduleProcessor;
    private ProcessorFactory processorFactory;

    private Manifest manifest = null;
    private RResource rResource = null;

    @Inject
    public RoboticsProcessor(ModuleProcessor moduleProcessor,
                             ProcessorFactory processorFactory) {
        this.moduleProcessor = moduleProcessor;
        this.processorFactory = processorFactory;
    }

    public void processModule(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {
            if (astType.isAnnotated(RoboticsModule.class)) {
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
