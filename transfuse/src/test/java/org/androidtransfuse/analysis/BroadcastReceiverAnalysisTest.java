package org.androidtransfuse.analysis;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.model.ComponentDescriptor;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverAnalysisTest {

    @Inject
    private BroadcastReceiverAnalysis analysis;
    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType broadcastReceiverType;

    @BroadcastReceiver
    public class BroadcastReceiverTarget{}

    @Before
    public void setup(){
        TransfuseTestInjector.inject(this);

        broadcastReceiverType = astClassFactory.buildASTClassType(BroadcastReceiverTarget.class);
    }

    @Test
    public void testAnalysis(){
        ComponentDescriptor analyze = analysis.analyze(broadcastReceiverType);

        //todo:fill in
    }

}
