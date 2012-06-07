package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.ActivityAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ComponentGenerator;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ComponentProcessor {

    private Logger logger;
    private ActivityAnalysis activityAnalysis;
    private ComponentGenerator generator;
    private Application application;
    private ProcessorContext context;

    @Inject
    public ComponentProcessor(@Assisted ProcessorContext context,
                              @Assisted Application application,
                              Logger logger,
                              ActivityAnalysis activityAnalysis,
                              ComponentGenerator generator) {
        this.logger = logger;
        this.activityAnalysis = activityAnalysis;
        this.generator = generator;
        this.context = context;
        this.application = application;
    }

    public void processComponent(Collection<? extends ASTType> astTypes) {

        for (ASTType astType : astTypes) {

            try {
                ComponentDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType, application, context);

                if (activityDescriptor != null) {
                    generator.generate(activityDescriptor);
                }

            } catch (JClassAlreadyExistsException e) {
                logger.error("JClassAlreadyExistsException while generating activity", e);
            }
        }
    }
}
