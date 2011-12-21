package org.androidrobotics.gen;

import android.os.Bundle;
import android.view.MotionEvent;
import com.sun.codemodel.*;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.astAnalyzer.MethodCallbackAspect;
import org.androidrobotics.gen.variableBuilder.ContextVariableBuilder;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
@Singleton
public class ActivityGenerator {

    private JCodeModel codeModel;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private Provider<ContextVariableBuilder> contextVariableBuilderProvider;
    private VariableBuilderRepositoryFactory variableBuilderRepositoryFactory;

    @Inject
    public ActivityGenerator(JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator, Provider<ContextVariableBuilder> contextVariableBuilderProvider, VariableBuilderRepositoryFactory variableBuilderRepositoryFactory) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
    }

    public void generate(ActivityDescriptor descriptor) throws IOException, JClassAlreadyExistsException, ClassNotFoundException {

        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

        definedClass._extends(android.app.Activity.class);


        JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
        JVar savedInstanceState = onCreateMethod.param(Bundle.class, "savedInstanceState");

        JBlock block = onCreateMethod.body();

        //super call with saved instance state
        block.invoke(JExpr._super(), onCreateMethod).arg(savedInstanceState);

        //layout setting
        block.invoke("setContentView").arg(JExpr.lit(descriptor.getLayout()));

        //injector and injection points
        //todo: more than one?
        if (descriptor.getInjectionPoints().size() > 0) {
            FieldInjectionPoint fieldInjectionPoint = descriptor.getInjectionPoints().get(0);

            Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, fieldInjectionPoint.getInjectionNode());

            addMethodCallbacks(block, "onCreate", expressionMap);

            //ontouch method

            JMethod onTouchEventMethod = definedClass.method(JMod.PUBLIC, codeModel.BOOLEAN, "onTouchEvent");
            onTouchEventMethod.param(MotionEvent.class, "motionEvent");
            JBlock onTouchBody = onTouchEventMethod.body();

            addMethodCallbacks(onTouchBody, "onTouch", expressionMap);
            onTouchBody._return(JExpr.TRUE);
        }
    }

    private void addMethodCallbacks(JBlock block, String name, Map<InjectionNode, JExpression> expressionMap) {
        for (Map.Entry<InjectionNode, JExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            MethodCallbackAspect methodCallbackToken = injectionNodeJExpressionEntry.getKey().getAspect(MethodCallbackAspect.class);

            if (methodCallbackToken != null) {
                Set<ASTMethod> astMethods = methodCallbackToken.getMethods(name);

                if (astMethods != null) {
                    for (ASTMethod astMethod : methodCallbackToken.getMethods(name)) {
                        block.add(injectionNodeJExpressionEntry.getValue().invoke(astMethod.getName()));
                    }
                }
            }
        }
    }
}
