package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.DeclareField;

/**
 * Analysis to determine if a type is annotated with @DeclareField.  If so, it set the AssignmentType of the
 * ASTInjectionAspect to FIELD.
 *
 * @author John Ericksen
 */
public class DeclareFieldAnalysis extends ASTAnalysisAdaptor {

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        if (astType.isAnnotated(DeclareField.class)) {
            ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);

            if (injectionAspect == null) {
                injectionAspect = new ASTInjectionAspect();
                injectionNode.addAspect(ASTInjectionAspect.class, injectionAspect);
            }

            injectionAspect.setAssignmentType(ASTInjectionAspect.InjectionAssignmentType.FIELD);
        }
    }
}
