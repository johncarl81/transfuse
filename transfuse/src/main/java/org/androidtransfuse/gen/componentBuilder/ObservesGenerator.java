package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ObservesAspect;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventObserver;
import org.androidtransfuse.event.WeakObserver;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ObservesGenerator implements ExpressionVariableDependentGenerator {

    private JCodeModel codeModel;
    private UniqueVariableNamer namer;

    @Inject
    public ObservesGenerator(JCodeModel codeModel, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.namer = namer;
    }

    @Override
    public void generate(JDefinedClass definedClass, JBlock block, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {

        JVar eventManager = block.decl(codeModel.ref(EventManager.class), "eventManager", JExpr._new(codeModel.ref(EventManager.class)));

        for (Map.Entry<InjectionNode, TypedExpression> injectionNodeTypedExpressionEntry : expressionMap.entrySet()) {

            InjectionNode injectionNode = injectionNodeTypedExpressionEntry.getKey();
            TypedExpression typedExpression = injectionNodeTypedExpressionEntry.getValue();

            if(injectionNode.containsAspect(ObservesAspect.class)){
                ObservesAspect aspect = injectionNode.getAspect(ObservesAspect.class);

                for (ASTType event : aspect.getEvents()) {

                    //generate WeakObserver<E, T> (E = event, T = target injection node)
                    JClass eventRef = codeModel.ref(event.getName());
                    JClass targetRef = codeModel.ref(typedExpression.getType().getName());
                    //todo:move into named inner class
                    JDefinedClass observerAnon = codeModel.anonymousClass(
                            codeModel.ref(WeakObserver.class)
                                    .narrow(eventRef)
                                    .narrow(targetRef));

                    JMethod triggerMethod = observerAnon.method(JMod.PUBLIC, codeModel.VOID, EventObserver.TRIGGER);
                    JVar eventParam = triggerMethod.param(eventRef, namer.generateName(event));
                    JVar targetParam = triggerMethod.param(targetRef, namer.generateName(typedExpression.getType()));
                    JBlock triggerBody = triggerMethod.body();

                    for (ASTMethod observerMethod : aspect.getObserverMethods(event)) {
                        triggerBody.invoke(targetParam, observerMethod.getName()).arg(eventParam);
                    }

                    JVar observer = block.decl(observerAnon, namer.generateName(WeakObserver.class),
                            JExpr._new(observerAnon).arg(typedExpression.getExpression()));

                    //register

                    block.invoke(eventManager, EventManager.REGISTER_METHOD).arg(eventRef.dotclass()).arg(observer);

                }
            }
        }
    }
}
