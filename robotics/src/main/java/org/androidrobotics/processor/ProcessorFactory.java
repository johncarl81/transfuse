package org.androidrobotics.processor;

import org.androidrobotics.analysis.ModuleProcessor;
import org.androidrobotics.model.manifest.Application;
import org.androidrobotics.model.manifest.Manifest;
import org.androidrobotics.model.r.RResource;

/**
 * @author John Ericksen
 */
public interface ProcessorFactory {
    ApplicationProcessor buildApplicationProcessor(ProcessorContext context);

    ComponentProcessor buildComponentProcessor(ProcessorContext context, Application application);

    RoboticsAssembler buildAssembler(ProcessorContext context);

    ProcessorContext buildContext(RResource rResource, Manifest manifest, ModuleProcessor moduleProcessor);
}
