package org.androidtransfuse.gen;

import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.analysis.BroadcastReceiverAnalysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ComponentGenerator;
import org.androidtransfuse.gen.Generator;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverGenerator implements Generator {

    private BroadcastReceiverAnalysis broadcastReceiverAnalysis;
    private ComponentGenerator generator;
    private Logger logger;

    @Inject
    public BroadcastReceiverGenerator(BroadcastReceiverAnalysis broadcastReceiverAnalysis,
                                      ComponentGenerator generator,
                                      Logger logger) {
        this.broadcastReceiverAnalysis = broadcastReceiverAnalysis;
        this.generator = generator;
        this.logger = logger;
    }

    @Override
    public void generate(ASTType type) {
        try {
            ComponentDescriptor broadcastDescriptor = broadcastReceiverAnalysis.analyzeElement(type);

            if (broadcastDescriptor != null) {
                generator.generate(broadcastDescriptor);
            }

        } catch (JClassAlreadyExistsException e) {
            logger.error("JClassAlreadyExistsException while generating activity", e);
        }
    }
}
