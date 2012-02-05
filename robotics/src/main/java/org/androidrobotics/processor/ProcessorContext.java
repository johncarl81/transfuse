package org.androidrobotics.processor;

import com.google.inject.assistedinject.Assisted;
import org.androidrobotics.analysis.ModuleProcessor;
import org.androidrobotics.model.manifest.Manifest;
import org.androidrobotics.model.r.RResource;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProcessorContext {

    private Manifest manifest;
    private Manifest sourceManifest = new Manifest();
    private RResource rResource;
    private ModuleProcessor moduleProcessor;

    @Inject
    public ProcessorContext(@Assisted RResource rResource, @Assisted Manifest manifest, @Assisted ModuleProcessor moduleProcessor) {
        this.manifest = manifest;
        this.rResource = rResource;
        this.moduleProcessor = moduleProcessor;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public RResource getRResource() {
        return rResource;
    }

    public ModuleProcessor getModuleProcessor() {
        return moduleProcessor;
    }

    public Manifest getSourceManifest() {
        return sourceManifest;
    }
}
