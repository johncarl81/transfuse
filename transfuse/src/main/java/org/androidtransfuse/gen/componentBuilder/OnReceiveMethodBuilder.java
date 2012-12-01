package org.androidtransfuse.gen.componentBuilder;

import android.content.Context;
import android.content.Intent;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.MethodDescriptorBuilder;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class OnReceiveMethodBuilder implements MethodBuilder {

    private final JCodeModel codeModel;
    private final ASTClassFactory astClassFactory;
    private final UniqueVariableNamer namer;

    @Inject
    public OnReceiveMethodBuilder(JCodeModel codeModel, ASTClassFactory astClassFactory, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.astClassFactory = astClassFactory;
        this.namer = namer;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        try {
            JMethod onReceiveMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onReceive");
            ASTMethod onReceiveASTMethod = astClassFactory.getMethod(android.content.BroadcastReceiver.class.getDeclaredMethod("onReceive", Context.class, Intent.class));

            MethodDescriptorBuilder methodDescriptor = new MethodDescriptorBuilder(onReceiveMethod, onReceiveASTMethod);

            for (ASTParameter astParameter : onReceiveASTMethod.getParameters()) {
                JVar param = onReceiveMethod.param(codeModel.ref(astParameter.getASTType().getName()), namer.generateName(astParameter.getASTType()));
                methodDescriptor.putParameter(astParameter, new TypedExpression(astParameter.getASTType(), param));
            }

            return methodDescriptor.build();
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while looking up onReceive method", e);
        }
    }

    @Override
    public void closeMethod(MethodDescriptor descriptor) {
        //no close necessary
    }
}
