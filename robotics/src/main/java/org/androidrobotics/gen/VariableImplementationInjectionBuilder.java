package org.androidrobotics.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisDependencyProcessingCallback;
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

    @Inject
    public VariableImplementationInjectionBuilder(@Assisted Class<?> implementationClass, JCodeModel codeModel, UniqueVariableNamer variableNamer, InjectionInvocationBuilder injectionInvocationBuilder, ASTClassFactory astClassFactory) {
        super(codeModel, variableNamer, injectionInvocationBuilder);

        this.implementationClass = implementationClass;
        this.astClassFactory = astClassFactory;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext) {
        return innerBuildVariable(implementationClass.getName(), injectionBuilderContext);
    }

    @Override
    public InjectionNode processInjectionNode(ASTType astType, AnalysisDependencyProcessingCallback callback) {
        return callback.processInjectionNode(astClassFactory.buildASTClassType(implementationClass));
    }
}
