package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
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
    private UniqueVariableNamer uniqueVariableNamer;
    private LayoutBuilder layoutBuilder;
    private ComponentBuilderFactory componentBuilderFactory;
    private ASTMethod onCreateASTMethod;

    @Inject
    public OnCreateComponentBuilder(@Assisted @Nullable InjectionNode injectionNode,
                                    @Assisted LayoutBuilder layoutBuilder,
                                    @Assisted ASTMethod onCreateASTMethod,
                                    InjectionFragmentGenerator injectionFragmentGenerator,
                                    JCodeModel codeModel,
                                    UniqueVariableNamer uniqueVariableNamer,
                                    ComponentBuilderFactory componentBuilderFactory,
                                    ASTClassFactory astClassFactory) {
        this.injectionNode = injectionNode;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.codeModel = codeModel;
        this.componentBuilderFactory = componentBuilderFactory;
        this.onCreateASTMethod = onCreateASTMethod;
        this.uniqueVariableNamer = uniqueVariableNamer;
        this.layoutBuilder = layoutBuilder;
    }

    @Override
    public void build(JDefinedClass definedClass, RResource rResource) {
        try {
            if (injectionNode != null) {

                final JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
                MethodDescriptor onCreateMethodDescriptor = new MethodDescriptor(onCreateMethod, onCreateASTMethod);

                List<JVar> parameters = new ArrayList<JVar>();

                for (ASTParameter methodArgument : onCreateASTMethod.getParameters()) {
                    JVar param = onCreateMethod.param(codeModel.ref(methodArgument.getASTType().getName()), uniqueVariableNamer.generateName(methodArgument.getName()));
                    parameters.add(param);
                    onCreateMethodDescriptor.putParameter(methodArgument, param);
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

                MethodGenerator onCreateMethodGenerator = new ExistingMethodGenerator(onCreateMethodDescriptor);
                MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator("onCreate", onCreateMethodGenerator);

                onCreateCallbackGenerator.addLifecycleMethod(definedClass, expressionMap);

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
