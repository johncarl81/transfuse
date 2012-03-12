package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.astAnalyzer.MethodCallbackAspect;
import org.androidtransfuse.config.Nullable;
import org.androidtransfuse.gen.ComponentBuilder;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import java.util.*;

/**
 * @author John Ericksen
 */
public class OnCreateComponentBuilder implements ComponentBuilder {

    private InjectionNode injectionNode;
    private JCodeModel codeModel;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private Set<MethodCallbackGenerator> methodCallbackGenerators = new HashSet<MethodCallbackGenerator>();
    private List<Class> methodArguments;
    private UniqueVariableNamer uniqueVariableNamer;
    private LayoutBuilder layoutBuilder;

    @Inject
    public OnCreateComponentBuilder(@Assisted @Nullable InjectionNode injectionNode, @Assisted LayoutBuilder layoutBuilder, InjectionFragmentGenerator injectionFragmentGenerator, JCodeModel codeModel, UniqueVariableNamer uniqueVariableNamer, @Assisted @Nullable Class<?>... methodArguments) {
        this.injectionNode = injectionNode;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.codeModel = codeModel;
        this.methodArguments = new ArrayList<Class>();

        if (methodArguments != null) {
            this.methodArguments.addAll(Arrays.asList(methodArguments));
        }

        this.uniqueVariableNamer = uniqueVariableNamer;
        this.layoutBuilder = layoutBuilder;
    }

    @Override
    public void build(JDefinedClass definedClass, RResource rResource) {
        try {
            if (injectionNode != null) {

                final JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");

                List<JVar> parameters = new ArrayList<JVar>();

                for (Class methodArgument : methodArguments) {
                    parameters.add(onCreateMethod.param(methodArgument, uniqueVariableNamer.generateName(methodArgument)));
                }

                //super.onCreate()
                JBlock block = onCreateMethod.body();
                JInvocation invocation = block.invoke(JExpr._super(), onCreateMethod);

                for (JVar parameter : parameters) {
                    invocation.arg(parameter);
                }

                //layout
                layoutBuilder.buildLayoutCall(block, rResource);

                Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode, rResource);

                for (Map.Entry<InjectionNode, JExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
                    MethodCallbackAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(MethodCallbackAspect.class);

                    if (methodCallbackAspect != null && methodCallbackAspect.contains("onCreate")) {
                        Set<MethodCallbackAspect.MethodCallback> methods = methodCallbackAspect.getMethodCallbacks("onCreate");

                        for (MethodCallbackAspect.MethodCallback methodCallback : methods) {
                            //todo: non-public access
                            block.add(injectionNodeJExpressionEntry.getValue().invoke(methodCallback.getMethod().getName()));
                        }
                    }
                }

                for (MethodCallbackGenerator methodCallbackGenerator : methodCallbackGenerators) {
                    methodCallbackGenerator.addLifecycleMethod(definedClass, expressionMap);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Injection Fragment", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while building Injection Fragment", e);
        }

    }

    public void addMethodCallbackBuilder(MethodCallbackGenerator methodCallbackGenerator) {
        this.methodCallbackGenerators.add(methodCallbackGenerator);
    }

    public void addMethodCallbackBuilders(Set<MethodCallbackGenerator> generators) {
        this.methodCallbackGenerators.addAll(generators);
    }
}
