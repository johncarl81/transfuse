package org.androidrobotics.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.model.manifest.Manifest;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class RoboticsAssembler {

    private JCodeModel codeModel;
    private Logger logger;
    private ProcessorContext context;

    @Inject
    public RoboticsAssembler(@Assisted ProcessorContext context, JCodeModel codeModel, Logger logger) {
        this.codeModel = codeModel;
        this.logger = logger;
        this.context = context;
    }

    public void writeSource(CodeWriter codeWriter, CodeWriter resourceWriter) {

        try {
            codeModel.build(
                    codeWriter,
                    resourceWriter);

        } catch (IOException e) {
            logger.error("Error while writing source files", e);
        }
    }

    public Manifest getManifest() {
        return context.getManifest();
    }
}
