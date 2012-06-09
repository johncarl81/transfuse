package org.androidtransfuse.analysis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ReceiveComponentBuilder implements ComponentBuilder{

    private JCodeModel codeModel;
    private UniqueVariableNamer namer;
    private InjectionNode injectionNode;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private ComponentBuilderFactory componentBuilderFactory;
    private ASTClassFactory astClassFactory;

    @Inject
    public ReceiveComponentBuilder(@Assisted InjectionNode injectionNode,
                                   JCodeModel codeModel,
                                   UniqueVariableNamer namer,
                                   InjectionFragmentGenerator injectionFragmentGenerator,
                                   ComponentBuilderFactory componentBuilderFactory,
                                   ASTClassFactory astClassFactory) {
        this.codeModel = codeModel;
        this.namer = namer;
        this.injectionNode = injectionNode;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
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

            Map<InjectionNode,TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(body, definedClass, injectionNode);

            MethodGenerator onReceiveMethodGenerator = new ExistingMethod(methodDescriptor);
            MethodCallbackGenerator onCreateCallbackGenerator = componentBuilderFactory.buildMethodCallbackGenerator("onReceive", onReceiveMethodGenerator);

            onCreateCallbackGenerator.generate(definedClass, body, expressionMap, descriptor);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
