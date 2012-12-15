package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.DeclareField;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
public class DeclareFieldAnalysisTest {

    private AnalysisContext analysisContext;
    @Inject
    private DeclareFieldAnalysis declareFieldAnalysis;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private InjectionPointFactory injectionPointFactory;
    @Inject
    private SimpleAnalysisContextFactory simpleAnalysisContextFactory;

    @DeclareField
    public class DeclareFieldTarget {
    }

    public class NonDeclareFieldTarget {
    }

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);

        analysisContext = simpleAnalysisContextFactory.buildContext();
    }

    @Test
    public void testLocalAssignmentType() {
        InjectionNode declareFieldInjectionNode = injectionPointFactory.buildInjectionNode(DeclareFieldTarget.class, analysisContext);

        declareFieldAnalysis.analyzeType(declareFieldInjectionNode, declareFieldInjectionNode.getASTType(), analysisContext);

        assertTrue(declareFieldInjectionNode.containsAspect(ASTInjectionAspect.class));
        assertEquals(ASTInjectionAspect.InjectionAssignmentType.FIELD, declareFieldInjectionNode.getAspect(ASTInjectionAspect.class).getAssignmentType());
    }

    @Test
    public void testFieldAssignmentType() {
        InjectionNode nonDeclareFieldInjectionNode = injectionPointFactory.buildInjectionNode(NonDeclareFieldTarget.class, analysisContext);

        declareFieldAnalysis.analyzeType(nonDeclareFieldInjectionNode, nonDeclareFieldInjectionNode.getASTType(), analysisContext);

        assertFalse(nonDeclareFieldInjectionNode.containsAspect(ASTInjectionAspect.class));

        nonDeclareFieldInjectionNode.addAspect(new ASTInjectionAspect());
        assertEquals(ASTInjectionAspect.InjectionAssignmentType.LOCAL, nonDeclareFieldInjectionNode.getAspect(ASTInjectionAspect.class).getAssignmentType());

        //test to make sure the default AssignmentType is not changed
        declareFieldAnalysis.analyzeType(nonDeclareFieldInjectionNode, nonDeclareFieldInjectionNode.getASTType(), analysisContext);
        assertTrue(nonDeclareFieldInjectionNode.containsAspect(ASTInjectionAspect.class));
        assertEquals(ASTInjectionAspect.InjectionAssignmentType.LOCAL, nonDeclareFieldInjectionNode.getAspect(ASTInjectionAspect.class).getAssignmentType());
    }
}
