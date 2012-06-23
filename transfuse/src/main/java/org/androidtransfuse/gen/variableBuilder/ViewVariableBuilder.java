package org.androidtransfuse.gen.variableBuilder;

import android.view.View;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.config.Nullable;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

public class ViewVariableBuilder extends ConsistentTypeVariableBuilder {

    private static final String FIND_VIEW_BY_ID = "findViewById";
    private static final String FIND_VIEW_BY_TAG = "findViewWithTag";

    private JType viewType;
    private Integer viewId;
    private String viewTag;
    private InjectionNode activityInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private RResourceReferenceBuilder rResourceReferenceBuilder;
    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private InvocationBuilder injectionInvocationBuilder;
    private RResource rResource;

    @Inject
    public ViewVariableBuilder(@Assisted @Nullable Integer viewId,
                               @Assisted @Nullable String viewTag,
                               @Assisted InjectionNode activityInjectionNode,
                               @Assisted JType viewType,
                               InjectionExpressionBuilder injectionExpressionBuilder,
                               RResourceReferenceBuilder rResourceReferenceBuilder,
                               JCodeModel codeModel,
                               InvocationBuilder injectionInvocationBuilder,
                               UniqueVariableNamer variableNamer,
                               RResource rResource,
                               TypedExpressionFactory typedExpressionFactory) {
        super(View.class, typedExpressionFactory);
        this.viewId = viewId;
        this.viewTag = viewTag;
        this.activityInjectionNode = activityInjectionNode;
        this.viewType = viewType;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.codeModel = codeModel;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.variableNamer = variableNamer;
        this.rResource = rResource;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        try {
            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, injectionNode);

            TypedExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

            JExpression viewExpression;
            if (viewId != null) {
                ResourceIdentifier viewResourceIdentifier = rResource.getResourceIdentifier(viewId);
                JExpression viewIdRef = rResourceReferenceBuilder.buildReference(viewResourceIdentifier);
                viewExpression = contextVar.getExpression().invoke(FIND_VIEW_BY_ID).arg(viewIdRef);
            } else {
                //viewTag is not null
                //<Activity>.getWindow().getDecorView().findViewWithTag(...)
                viewExpression = contextVar.getExpression()
                        .invoke("getWindow").invoke("getDecorView").invoke(FIND_VIEW_BY_TAG).arg(JExpr.lit(viewTag));
            }

            ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
            if (injectionAspect == null) {
                return viewExpression;
            } else {
                return inject(injectionAspect, injectionBuilderContext, injectionNode, viewExpression);
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while generating injection: " + injectionNode.getClassName(), e);
        }
    }

    public JVar inject(ASTInjectionAspect injectionAspect, InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode, JExpression viewExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        JVar variableRef;
        JType nodeType = codeModel.parseType(injectionNode.getClassName());

        if (injectionAspect.getAssignmentType().equals(ASTInjectionAspect.InjectionAssignmentType.LOCAL)) {
            variableRef = injectionBuilderContext.getBlock().decl(nodeType, variableNamer.generateName(injectionNode));
        } else {
            variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(injectionNode));
        }
        JBlock block = injectionBuilderContext.getBlock();


        block.assign(variableRef, JExpr.cast(viewType, viewExpression));

        //field injection
        for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
            block.add(
                    injectionInvocationBuilder.buildFieldSet(
                            injectionBuilderContext.getVariableMap().get(fieldInjectionPoint.getInjectionNode()),
                            fieldInjectionPoint,
                            variableRef));
        }

        //method injection
        for (MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
            block.add(
                    injectionInvocationBuilder.buildMethodCall(
                            Object.class.getName(),
                            injectionBuilderContext.getVariableMap(),
                            methodInjectionPoint,
                            variableRef));
        }

        return variableRef;
    }
}