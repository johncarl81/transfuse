package org.androidtransfuse.gen.componentBuilder;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTField;
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

    LayoutHandlerBuilder buildLayoutHandlerBuilder(InjectionNode layoutHandlerInjectionNode);

    OnCreateMethodBuilder buildOnCreateMethodBuilder(ASTMethod method, LayoutBuilder layoutBuilder);

    FragmentOnCreateViewMethodBuilder buildFragmentMethodBuilder(Integer layout, ASTMethod method);

    OnReceiveMethodBuilder buildOnReceiveMethodBuilder();

    BroadcastReceiverInjectionNodeFactory buildBroadcastReceiverInjectionNodeFactory(ASTType astType);

    InjectionNodeFactoryImpl buildInjectionNodeFactory(ASTType astType, AnalysisContext context);

    ViewRegistrationGenerator buildViewRegistrationGenerator(@Assisted("viewInjectionNode")InjectionNode viewInjectionNode, String listenerMethod, @Assisted("targetInjectionNode")InjectionNode injectionNode, ViewRegistrationInvocationBuilder invocationBuilder);

    ViewMethodRegistrationInvocationBuilderImpl buildViewMethodRegistrationInvocationBuilder(ASTMethod getterMethod);

    ViewFieldRegistrationInvocationBuilderImpl buildViewFieldRegistrationInvocationBuilder(ASTField field);

    ActivityDelegateRegistrationGenerator buildActivityRegistrationGenerator(ActivityDelegateASTReference activityDelegateASTReference, ImmutableList<ASTMethod> methods);

    ActivityTypeDelegateASTReference buildActivityTypeDelegateASTReference();

    ActivityMethodDelegateASTReference buildActivityMethodDelegateASTReference(ASTMethod astMethod);

    ActivityFieldDelegateASTReference buildActivityFieldDelegateASTReference(ASTField astField);
}
