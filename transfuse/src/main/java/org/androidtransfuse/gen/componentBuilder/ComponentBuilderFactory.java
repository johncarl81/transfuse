package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface ComponentBuilderFactory {

    MethodCallbackGenerator buildMethodCallbackGenerator(String eventName, MethodGenerator methodGenerator);

    RLayoutBuilder buildRLayoutBuilder(Integer layout);

    MirroredMethodGenerator buildMirroredMethodGenerator(ASTMethod method, boolean superCall);

    ReturningMethodGenerator buildReturningMethodGenerator(ASTMethod method, boolean superCall, JExpression expression);

    LayoutHandlerBuilder buildLayoutHandlerBuilder(InjectionNode layoutHandlerInjectionNode);

    OnCreateMethodBuilder buildOnCreateMethodBuilder(ASTMethod method, LayoutBuilder layoutBuilder);

    FragmentSimpleLayoutBuilder buildFragmentSimpleLayoutBuilder(Integer layout);

    FragmentOnCreateViewMethodBuilder buildFragmentMethodBuilder(Integer layout, ASTMethod method);

    OnReceiveMethodBuilder buildOnReceiveMethodBuilder();

    BroadcastReceiverInjectionNodeFactory buildBroadcastReceiverInjectionNodeFactory(ASTType astType);

    InjectionNodeFactoryImpl buildInjectionNodeFactory(ASTType astType, AnalysisContext context);

    ObservesGenerator buildObservesGenerator(AnalysisContext context);
}
