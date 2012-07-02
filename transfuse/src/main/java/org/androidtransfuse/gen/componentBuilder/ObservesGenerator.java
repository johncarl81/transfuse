package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ObservesAspect;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventObserver;
import org.androidtransfuse.event.WeakObserver;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
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
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private InjectionPointFactory injectionPointFactory;
    private AnalysisContext context;

    @Inject
    public ObservesGenerator(@Assisted AnalysisContext context,
                             JCodeModel codeModel,
                             UniqueVariableNamer namer,
                             InjectionFragmentGenerator injectionFragmentGenerator,
                             InjectionPointFactory injectionPointFactory) {
        this.codeModel = codeModel;
        this.namer = namer;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.injectionPointFactory = injectionPointFactory;
        this.context = context;
    }

    @Override
    public void generate(JDefinedClass definedClass, JBlock block, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {

        try{
            InjectionNode eventManagerInjectionNode = injectionPointFactory.buildInjectionNode(EventManager.class, context);
            Map<InjectionNode, TypedExpression> eventManagerExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, eventManagerInjectionNode);
            JExpression eventManager = eventManagerExpressionMap.get(eventManagerInjectionNode).getExpression();

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
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Observes functionality", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while building Observes functionality", e);
        }
    }
}
