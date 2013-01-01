/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
