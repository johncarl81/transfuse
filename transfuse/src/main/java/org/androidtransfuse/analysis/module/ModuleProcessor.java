package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.BindInterceptor;
import org.androidtransfuse.annotations.BindProvider;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ModuleProcessor {

    private Map<ASTType, MethodProcessor> methodProcessorMap = new HashMap<ASTType, MethodProcessor>();

    @Inject
    public ModuleProcessor(Provider<BindProcessor> bindProcessorProvider,
                           Provider<BindProviderProcessor> bindProviderProcessorProvider,
                           Provider<BindInterceptorProcessor> bindInterceptorProcessorProvider,
                           ASTClassFactory astClassFactory) {
        methodProcessorMap.put(astClassFactory.buildASTClassType(Bind.class), bindProcessorProvider.get());
        methodProcessorMap.put(astClassFactory.buildASTClassType(BindInterceptor.class), bindInterceptorProcessorProvider.get());
        methodProcessorMap.put(astClassFactory.buildASTClassType(BindProvider.class), bindProviderProcessorProvider.get());
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
