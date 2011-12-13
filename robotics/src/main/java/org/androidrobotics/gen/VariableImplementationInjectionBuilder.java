package org.androidrobotics.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableImplementationInjectionBuilder extends VariableInjectionBuilderBase {

    private Class<?> implementationClass;
    private ASTClassFactory astClassFactory;
    private Analyzer analyzer;

    @Inject
    public VariableImplementationInjectionBuilder(@Assisted Class<?> implementationClass, Analyzer analyzer, JCodeModel codeModel, UniqueVariableNamer variableNamer, InjectionInvocationBuilder injectionInvocationBuilder, ASTClassFactory astClassFactory) {
        super(codeModel, variableNamer, injectionInvocationBuilder);

        this.implementationClass = implementationClass;
        this.astClassFactory = astClassFactory;
        this.analyzer = analyzer;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext) {
        return innerBuildVariable(implementationClass.getName(), injectionBuilderContext);
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        return analyzer.analyze(astType, astClassFactory.buildASTClassType(implementationClass), context);
    }
}
