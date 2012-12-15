package org.androidtransfuse.analysis.module;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.BindInterceptor;
import org.androidtransfuse.annotations.BindProvider;
import org.androidtransfuse.processor.AbstractCompletionTransactionWorker;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Central module processor class.  Scans the input AST elements for the appropriate annotations and registers
 * the results with the given processor.  For instance:
 * <p/>
 * {@code
 *
 * @author John Ericksen
 * @TransfuseModule public interface IntegrationModule {
 * @Bind(LoopThreeImpl.class) LoopThree getThree();
 * }
 * }
 * <p/>
 * associates teh LoopThreeImpl class to be used when a LoopThree is injected.
 */
public class ModuleTransactionWorker extends AbstractCompletionTransactionWorker<Provider<ASTType>, Void> {

    private final ImmutableMap<ASTType, MethodProcessor> bindingProcessors;

    @Inject
    public ModuleTransactionWorker(BindProcessor bindProcessor,
                                   BindProviderProcessor bindProviderProcessor,
                                   BindInterceptorProcessor bindInterceptorProcessor,
                                   ASTClassFactory astClassFactory) {
        ImmutableMap.Builder<ASTType, MethodProcessor> moduleBindings = ImmutableMap.builder();
        moduleBindings.put(astClassFactory.getType(Bind.class), bindProcessor);
        moduleBindings.put(astClassFactory.getType(BindInterceptor.class), bindInterceptorProcessor);
        moduleBindings.put(astClassFactory.getType(BindProvider.class), bindProviderProcessor);

        this.bindingProcessors = moduleBindings.build();
    }

    @Override
    public Void innerRun(Provider<ASTType> astTypeProvider) {

        ASTType type = astTypeProvider.get();

        ImmutableList.Builder<ModuleConfiguration> configurations = ImmutableList.builder();

        for (ASTMethod astMethod : type.getMethods()) {
            for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {

                if (bindingProcessors.containsKey(astAnnotation.getASTType())) {
                    MethodProcessor methodProcessor = bindingProcessors.get(astAnnotation.getASTType());

                    configurations.add(methodProcessor.process(astMethod, astAnnotation));
                }
            }
        }

        for (ModuleConfiguration moduleConfiguration : configurations.build()) {
            moduleConfiguration.setConfiguration();
        }

        return null;
    }
}
