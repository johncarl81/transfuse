package org.androidtransfuse.analysis;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Service;
import org.androidtransfuse.model.ComponentDescriptor;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ServiceAnalysisTest {

    @Inject
    private ServiceAnalysis serviceAnalysis;
    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType serviceTargetType;

    @Service
    public class ServiceAnalysisTarget{}

    @Before
    public void setup(){
        TransfuseTestInjector.inject(this);

        serviceTargetType = astClassFactory.buildASTClassType(ServiceAnalysisTarget.class);
    }

    @Test
    public void testAnalysis(){
        ComponentDescriptor componentDescriptor = serviceAnalysis.analyze(serviceTargetType);

        //todo:fill in
    }

}
