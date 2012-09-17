package org.androidtransfuse.analysis.module;

import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.BindInterceptor;
import org.androidtransfuse.annotations.BindProvider;

import javax.inject.Inject;

/**
 * Central module processor class.  Scanns the input AST elements for the appropriate annotations and registers
 * the results with the given processor.  For instance:
 *
 * {@code
 * @TransfuseModule
 * public interface IntegrationModule {
 *     @Bind(LoopThreeImpl.class)
 *     LoopThree getThree();
 * }
 * }
 *
 * associates teh LoopThreeImpl class to be used when a LoopThree is injected.
 *
 * @author John Ericksen
 */
public class ModuleProcessor {

    private final ImmutableMap<ASTType, MethodProcessor> bindingProcessors;

    @Inject
    public ModuleProcessor(BindProcessor bindProcessor,
                           BindProviderProcessor bindProviderProcessor,
                           BindInterceptorProcessor bindInterceptorProcessor,
                           ASTClassFactory astClassFactory) {
        ImmutableMap.Builder<ASTType, MethodProcessor> moduleBindings = ImmutableMap.builder();
        moduleBindings.put(astClassFactory.buildASTClassType(Bind.class), bindProcessor);
        moduleBindings.put(astClassFactory.buildASTClassType(BindInterceptor.class), bindInterceptorProcessor);
        moduleBindings.put(astClassFactory.buildASTClassType(BindProvider.class), bindProviderProcessor);

        this.bindingProcessors = moduleBindings.build();
    }

    public void processMethod(ASTMethod astMethod) {

        for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {

            if (bindingProcessors.containsKey(astAnnotation.getASTType())) {
                MethodProcessor methodProcessor = bindingProcessors.get(astAnnotation.getASTType());

                methodProcessor.process(astMethod, astAnnotation);
            }
        }
    }
}
