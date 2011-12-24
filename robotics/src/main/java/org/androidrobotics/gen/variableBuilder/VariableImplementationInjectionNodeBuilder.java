package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableImplementationInjectionNodeBuilder implements InjectionNodeBuilder {

    private Analyzer analyzer;
    private ASTClassFactory astClassFactory;
    private Class<?> implementationClass;

    @Inject
    public VariableImplementationInjectionNodeBuilder(@Assisted Class<?> implementationClass,
                                                      Analyzer analyzer,
                                                      ASTClassFactory astClassFactory) {
        this.analyzer = analyzer;
        this.astClassFactory = astClassFactory;
        this.implementationClass = implementationClass;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        return analyzer.analyze(astType, astClassFactory.buildASTClassType(implementationClass), context);
    }
}
