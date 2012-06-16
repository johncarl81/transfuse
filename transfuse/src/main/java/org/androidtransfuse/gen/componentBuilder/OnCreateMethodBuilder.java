package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class OnCreateMethodBuilder implements MethodBuilder {

    private JCodeModel codeModel;
    private ASTMethod onCreateASTMethod;
    private UniqueVariableNamer namer;

    @Inject
    public OnCreateMethodBuilder(@Assisted ASTMethod onCreateASTMethod, JCodeModel codeModel, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.onCreateASTMethod = onCreateASTMethod;
        this.namer = namer;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        final JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
        MethodDescriptor onCreateMethodDescriptor = new MethodDescriptor(onCreateMethod, onCreateASTMethod);

        List<JVar> parameters = new ArrayList<JVar>();

        for (ASTParameter methodArgument : onCreateASTMethod.getParameters()) {
            JVar param = onCreateMethod.param(codeModel.ref(methodArgument.getASTType().getName()), namer.generateName(methodArgument.getASTType()));
            parameters.add(param);
            onCreateMethodDescriptor.putParameter(methodArgument, new TypedExpression(methodArgument.getASTType(), param));
        }

        //super.onCreate()
        JBlock block = onCreateMethod.body();
        JInvocation invocation = block.invoke(JExpr._super(), onCreateMethod);

        for (JVar parameter : parameters) {
            invocation.arg(parameter);
        }

        return onCreateMethodDescriptor;
    }
}
