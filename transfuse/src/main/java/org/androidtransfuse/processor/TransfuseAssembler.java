package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.security.PrivilegedActionException;

/**
 * @author John Ericksen
 */
public class TransfuseAssembler {

    private JCodeModel codeModel;
    private Logger logger;
    private ProcessorContext context;
    private Merger merger;

    @Inject
    public TransfuseAssembler(@Assisted ProcessorContext context, JCodeModel codeModel, Logger logger, Merger merger) {
        this.codeModel = codeModel;
        this.logger = logger;
        this.context = context;
        this.merger = merger;
    }

    public void writeSource(CodeWriter codeWriter, CodeWriter resourceWriter) {

        try {
            codeModel.build(
                    codeWriter,
                    resourceWriter);

        } catch (IOException e) {
            logger.error("IOException while writing source files", e);
        }
    }

    public Manifest buildManifest() {

        try {
            return merger.merge(context.getManifest(), context.getSourceManifest());
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException while merging manifest", e);
        } catch (PrivilegedActionException e) {
            logger.error("PrivilegedActionException while merging manifest", e);
        } catch (InstantiationException e) {
            logger.error("InstantiationException while merging manifest", e);
        }

        return context.getManifest();
    }
}
