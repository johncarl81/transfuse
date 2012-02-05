package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.ModuleProcessor;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;

/**
 * @author John Ericksen
 */
public interface ProcessorFactory {
    ApplicationProcessor buildApplicationProcessor(ProcessorContext context);

    ComponentProcessor buildComponentProcessor(ProcessorContext context, Application application);

    TransfuseAssembler buildAssembler(ProcessorContext context);

    ProcessorContext buildContext(RResource rResource, Manifest manifest, ModuleProcessor moduleProcessor);
}
