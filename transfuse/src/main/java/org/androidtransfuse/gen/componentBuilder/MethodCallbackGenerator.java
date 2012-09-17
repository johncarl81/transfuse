package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.astAnalyzer.ListenerAspect;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class MethodCallbackGenerator implements ExpressionVariableDependentGenerator {

    private final String name;
    private final MethodGenerator methodGenerator;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public MethodCallbackGenerator(@Assisted String name, @Assisted MethodGenerator methodGenerator, InvocationBuilder invocationBuilder) {
        this.name = name;
        this.methodGenerator = methodGenerator;
        this.invocationBuilder = invocationBuilder;
    }

    public void generate(JDefinedClass definedClass, MethodDescriptor creationMethodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {
        try {
            MethodDescriptor methodDescriptor = null;
            for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
                ListenerAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(ListenerAspect.class);

                if (methodCallbackAspect != null && methodCallbackAspect.contains(name)) {
                    Set<ASTMethod> methods = methodCallbackAspect.getListeners(name);

                    //define method on demand for possible lazy init
                    if (methodDescriptor == null) {
                        methodDescriptor = methodGenerator.buildMethod(definedClass);
                    }
                    JBlock body = methodDescriptor.getMethod().body();

                    for (ASTMethod methodCallback : methods) {

                        JStatement methodCall = invocationBuilder.buildMethodCall(
                                Object.class.getName(),
                                methodDescriptor.getASTMethod().getParameters(),
                                methodDescriptor.getParameters(),
                                injectionNodeJExpressionEntry.getValue().getType(),
                                injectionNodeJExpressionEntry.getValue().getExpression(),
                                methodCallback);

                        body.add(methodCall);
                    }
                }
            }

            methodGenerator.closeMethod(methodDescriptor);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building method call", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while generating method call.", e);
        }

    }
}
