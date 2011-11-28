package org.androidrobotics;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ActivityAnalysis;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.ActivityGenerator;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class RoboticsProcessor {

    @Inject
    private ActivityAnalysis activityAnalysis;
    @Inject
    private ActivityGenerator activityGenerator;
    @Inject
    private JCodeModel codeModel;
    @Inject
    private Logger logger;

    public void processModuleElements(Collection<? extends ASTType> astTypes) {
        for (ASTType astType : astTypes) {
            //todo: module configuration
        }
    }

    public void processRootElement(Collection<? extends ASTType> astTypes) {
        for (ASTType astType : astTypes) {
            ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(astType);

            if (activityDescriptor != null) {
                try {
                    activityGenerator.generate(activityDescriptor);
                } catch (IOException e) {
                    logger.error("IOException while generating activity", e);
                } catch (JClassAlreadyExistsException e) {
                    logger.error("IOException while generating activity", e);
                } catch (ClassNotFoundException e) {
                    logger.error("IOException while generating activity", e);
                }
            }
        }
    }

    public void verify() {

    }

    public void writeSource(CodeWriter codeWriter, CodeWriter recourceWriter) {

        try {
            codeModel.build(
                    codeWriter,
                    recourceWriter);

        } catch (IOException e) {
            logger.error("Error while writing source files", e);
        }

    }


}
