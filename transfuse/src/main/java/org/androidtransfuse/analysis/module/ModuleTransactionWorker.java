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
import org.androidtransfuse.annotations.Interceptors;
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

    private final ImmutableMap<ASTType, MethodProcessor> methodProcessors;
    private final ImmutableMap<ASTType, TypeProcessor> typeProcessors;

    @Inject
    public ModuleTransactionWorker(BindProcessor bindProcessor,
                                   BindProviderProcessor bindProviderProcessor,
                                   BindInterceptorProcessor bindInterceptorProcessor,
                                   BindInterceptorsProcessor bindInterceptorsProcessor,
                                   ASTClassFactory astClassFactory) {
        ImmutableMap.Builder<ASTType, MethodProcessor> methodProcessorsBuilder = ImmutableMap.builder();
        methodProcessorsBuilder.put(astClassFactory.getType(Bind.class), bindProcessor);
        methodProcessorsBuilder.put(astClassFactory.getType(BindProvider.class), bindProviderProcessor);

        this.methodProcessors = methodProcessorsBuilder.build();

        ImmutableMap.Builder<ASTType, TypeProcessor> typeProcessorsBuilder = ImmutableMap.builder();
        typeProcessorsBuilder.put(astClassFactory.getType(Interceptors.class), bindInterceptorsProcessor);
        typeProcessorsBuilder.put(astClassFactory.getType(BindInterceptor.class), bindInterceptorProcessor);

        typeProcessors = typeProcessorsBuilder.build();
    }

    @Override
    public Void innerRun(Provider<ASTType> astTypeProvider) {

        ASTType type = astTypeProvider.get();

        ImmutableList.Builder<ModuleConfiguration> configurations = ImmutableList.builder();

        for (ASTAnnotation typeAnnotation : type.getAnnotations()) {
            if(typeProcessors.containsKey(typeAnnotation.getASTType())){
                TypeProcessor typeProcessor = typeProcessors.get(typeAnnotation.getASTType());

                configurations.add(typeProcessor.process(typeAnnotation));
            }
        }

        for (ASTMethod astMethod : type.getMethods()) {
            for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {

                if (methodProcessors.containsKey(astAnnotation.getASTType())) {
                    MethodProcessor methodProcessor = methodProcessors.get(astAnnotation.getASTType());

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
