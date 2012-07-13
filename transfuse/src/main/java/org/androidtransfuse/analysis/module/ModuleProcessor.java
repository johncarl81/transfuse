package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.BindInterceptor;
import org.androidtransfuse.annotations.BindProvider;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

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

    private Map<ASTType, MethodProcessor> methodProcessorMap = new HashMap<ASTType, MethodProcessor>();

    @Inject
    public ModuleProcessor(BindProcessor bindProcessor,
                           BindProviderProcessor bindProviderProcessor,
                           BindInterceptorProcessor bindInterceptorProcessor,
                           ASTClassFactory astClassFactory) {
        methodProcessorMap.put(astClassFactory.buildASTClassType(Bind.class), bindProcessor);
        methodProcessorMap.put(astClassFactory.buildASTClassType(BindInterceptor.class), bindInterceptorProcessor);
        methodProcessorMap.put(astClassFactory.buildASTClassType(BindProvider.class), bindProviderProcessor);
    }

    public void processMethod(ASTMethod astMethod) {

        for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {

            if (methodProcessorMap.containsKey(astAnnotation.getASTType())) {
                MethodProcessor methodProcessor = methodProcessorMap.get(astAnnotation.getASTType());

                methodProcessor.process(astMethod, astAnnotation);
            }
        }
    }
}
