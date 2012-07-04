package org.androidtransfuse.gen.variableDecorator;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTProxyType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ObservesAspect;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventObserver;
import org.androidtransfuse.event.WeakObserver;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableBuilder.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ObservesExpressionDecorator extends VariableExpressionBuilderDecorator {

    private static final String SUPER_REF = "super";

    private JCodeModel codeModel;
    private UniqueVariableNamer namer;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private InjectionPointFactory injectionNodeFactory;
    private TypedExpressionFactory typedExpressionFactory;

    @Inject
    public ObservesExpressionDecorator(@Assisted VariableExpressionBuilder decorated,
                                       JCodeModel codeModel,
                                       UniqueVariableNamer namer,
                                       InjectionExpressionBuilder injectionExpressionBuilder,
                                       InjectionPointFactory injectionNodeFactory, TypedExpressionFactory typedExpressionFactory) {
        super(decorated);
        this.codeModel = codeModel;
        this.namer = namer;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.injectionNodeFactory = injectionNodeFactory;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        TypedExpression typedExpression = getDecorated().buildVariableExpression(injectionBuilderContext, injectionNode);

        if(injectionNode.containsAspect(ObservesAspect.class)){
            try {
                JBlock block = injectionBuilderContext.getBlock();
                JDefinedClass definedClass = injectionBuilderContext.getDefinedClass();
                ObservesAspect aspect = injectionNode.getAspect(ObservesAspect.class);

                for (ASTType event : aspect.getEvents()) {

                    //generate WeakObserver<E, T> (E = event, T = target injection node)
                    JClass eventRef = codeModel.ref(event.getName());
                    JClass targetRef = codeModel.ref(typedExpression.getType().getName());

                    JDefinedClass observerClass = definedClass._class(JMod.PROTECTED | JMod.STATIC | JMod.FINAL, namer.generateName(typedExpression.getType()));

                    //match default constructor public WeakObserver(T target){
                    JMethod constructor = observerClass.constructor(JMod.PUBLIC);
                    JVar constTargetParam = constructor.param(targetRef, namer.generateClassName(targetRef));
                    constructor.body().invoke(SUPER_REF).arg(constTargetParam);

                    observerClass._extends(
                            codeModel.ref(WeakObserver.class)
                                    .narrow(eventRef)
                                    .narrow(targetRef));


                    JMethod triggerMethod = observerClass.method(JMod.PUBLIC, codeModel.VOID, EventObserver.TRIGGER);
                    JVar eventParam = triggerMethod.param(eventRef, namer.generateName(event));
                    JVar targetParam = triggerMethod.param(targetRef, namer.generateName(typedExpression.getType()));
                    JBlock triggerBody = triggerMethod.body();

                    for (ASTMethod observerMethod : aspect.getObserverMethods(event)) {
                        triggerBody.invoke(targetParam, observerMethod.getName()).arg(eventParam);
                    }

                    JFieldVar observer = definedClass.field(JMod.PRIVATE, observerClass, namer.generateName(EventObserver.class));

                    block.assign(observer, JExpr._new(observerClass).arg(typedExpression.getExpression()));

                    block.invoke(observer, "setEventManager").arg(getEventManager(injectionBuilderContext, aspect));

                    //add onPause callback
                    InjectionNode weakObserverInjectionNode = new InjectionNode(
                            new ASTProxyType(aspect.getObserverInjectionNode().getASTType(), observerClass.name()));

                    weakObserverInjectionNode.getAspects().putAll(aspect.getObserverInjectionNode().getAspects());

                    injectionBuilderContext.getVariableMap().put(weakObserverInjectionNode, typedExpressionFactory.build(WeakObserver.class, observer));

                    //register
                    block.invoke(getEventManager(injectionBuilderContext, aspect), EventManager.REGISTER_METHOD).arg(eventRef.dotclass()).arg(observer);
                }
            } catch (JClassAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
        return typedExpression;
    }

    private JExpression getEventManager(InjectionBuilderContext injectionBuilderContext, ObservesAspect aspect) {
        return injectionExpressionBuilder.buildVariable(injectionBuilderContext, aspect.getEventManagerInjectionNode()).getExpression();
    }
}
