package org.androidtransfuse.analysis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.gen.variableBuilder.StaticExpressionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ReceiveComponentBuilder implements ComponentBuilder {

    private JCodeModel codeModel;
    private UniqueVariableNamer namer;
    private ASTType astType;
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private ComponentBuilderFactory componentBuilderFactory;
    private ASTClassFactory astClassFactory;
    private AnalysisContextFactory analysisContextFactory;
    private InjectionPointFactory injectionPointFactory;
    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ReceiveComponentBuilder(@Assisted InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                                   @Assisted ASTType astType,
                                   JCodeModel codeModel,
                                   UniqueVariableNamer namer,
                                   InjectionFragmentGenerator injectionFragmentGenerator,
                                   ComponentBuilderFactory componentBuilderFactory,
                                   ASTClassFactory astClassFactory,
                                   AnalysisContextFactory analysisContextFactory,
                                   InjectionPointFactory injectionPointFactory,
                                   InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory, VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.codeModel = codeModel;
        this.namer = namer;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
        this.astType = astType;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public void build(JDefinedClass definedClass, ComponentDescriptor descriptor) {

        try {
            //void onReceive(android.content.Context context, android.content.Intent intent);
            JMethod onReceiveMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onReceive");
            ASTMethod onReceiveASTMethod = astClassFactory.buildASTClassMethod(BroadcastReceiver.class.getDeclaredMethod("onReceive", Context.class, Intent.class));

            MethodDescriptor methodDescriptor = new MethodDescriptor(onReceiveMethod, onReceiveASTMethod);

            for (ASTParameter astParameter : onReceiveASTMethod.getParameters()) {
                JVar param = onReceiveMethod.param(codeModel.ref(astParameter.getASTType().getName()), namer.generateName(astParameter.getASTType()));
                methodDescriptor.putParameter(astParameter, new TypedExpression(astParameter.getASTType(), param));
            }
            JBlock body = onReceiveMethod.body();

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(methodDescriptor));
            InjectionNode injectionNode = injectionPointFactory.buildInjectionNode(astType, context);

            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(body, definedClass, injectionNode);

            MethodGenerator onReceiveMethodGenerator = new ExistingMethod(methodDescriptor);
            MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator("onReceive", onReceiveMethodGenerator);

            onCreateCallbackGenerator.generate(definedClass, body, expressionMap, descriptor);

        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Injection Fragment", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while building Injection Fragment", e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while looking up onRecieve Method", e);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(MethodDescriptor methodDescriptor) {

        InjectionNodeBuilderRepository repository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilderRepository);
        repository.put(android.content.BroadcastReceiver.class.getName(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(org.androidtransfuse.annotations.BroadcastReceiver.class));

        for (Map.Entry<ASTParameter, TypedExpression> parameterEntry : methodDescriptor.getParameters().entrySet()) {
            repository.put(parameterEntry.getKey().getASTType().getName(), new StaticExpressionNodeBuilder(parameterEntry.getValue()));
        }

        return repository;
    }
}
