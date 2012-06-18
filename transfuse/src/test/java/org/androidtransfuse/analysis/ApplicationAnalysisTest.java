package org.androidtransfuse.analysis;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Application;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ApplicationAnalysisTest {

    @Inject
    private ApplicationAnalysis applicationAnalysis;
    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType applicationASTType;

    @Application
    public class ApplicationAnalysisTarget{}

    @Before
    public void setup(){
        TransfuseTestInjector.inject(this);

        applicationASTType = astClassFactory.buildASTClassType(ApplicationAnalysisTarget.class);
    }

    @Test
    public void testAnalysis(){
        applicationAnalysis.analyze(applicationASTType);
    }

}
