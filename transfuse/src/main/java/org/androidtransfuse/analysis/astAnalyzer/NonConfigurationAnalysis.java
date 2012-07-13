package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.NonConfigurationInstance;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * Analysis class to identify nonConfiguration Instances(NCI).  This analysis triggers the given NCI to be defined as
 * a field in the containing component.  This field is then used in the generated onRetainNonConfigurationInstance() and
 * getLastNonConfigurationInstance() methods.
 *
 * @author John Ericksen
 */
public class NonConfigurationAnalysis extends ASTAnalysisAdaptor {

    private InjectionPointFactory injectionPointFactory;

    @Inject
    public NonConfigurationAnalysis(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTType concreteType, ASTField astField, AnalysisContext context) {

        if(astField.isAnnotated(NonConfigurationInstance.class)){
            NonConfigurationAspect aspect = buildAspect(injectionNode);
            aspect.add(injectionPointFactory.buildInjectionPoint(concreteType, astField, context));

            if (!injectionNode.containsAspect(ASTInjectionAspect.class)) {
                injectionNode.addAspect(ASTInjectionAspect.class, new ASTInjectionAspect());
            }

            injectionNode.getAspect(ASTInjectionAspect.class).setAssignmentType(ASTInjectionAspect.InjectionAssignmentType.FIELD);
        }
    }

    private NonConfigurationAspect buildAspect(InjectionNode injectionNode){
        if(!injectionNode.containsAspect(NonConfigurationAspect.class)){
            injectionNode.addAspect(new NonConfigurationAspect());
        }
        return injectionNode.getAspect(NonConfigurationAspect.class);
    }
}
