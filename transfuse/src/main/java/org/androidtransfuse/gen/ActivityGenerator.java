package org.androidtransfuse.gen;

import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.ActivityAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ComponentGenerator;
import org.androidtransfuse.gen.Generator;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ActivityGenerator implements Generator {


    private ActivityAnalysis activityAnalysis;
    private ComponentGenerator componentGenerator;
    private Logger logger;

    @Inject
    public ActivityGenerator(ActivityAnalysis activityAnalysis,
                             ComponentGenerator componentGenerator,
                             Logger logger) {
        this.activityAnalysis = activityAnalysis;
        this.componentGenerator = componentGenerator;
        this.logger = logger;
    }

    @Override
    public void generate(ASTType astType) {
        try {
            ComponentDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType);

            if (activityDescriptor != null) {
                componentGenerator.generate(activityDescriptor);
            }

        } catch (JClassAlreadyExistsException e) {
            logger.error("JClassAlreadyExistsException while generating activity", e);
        }
    }
}
