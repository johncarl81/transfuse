package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.module.BindInterceptorProcessor;
import org.androidtransfuse.analysis.module.BindProcessor;
import org.androidtransfuse.analysis.module.BindProviderProcessor;
import org.androidtransfuse.analysis.module.MethodProcessor;
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

    private Map<String, MethodProcessor> methodProcessorMap = new HashMap<String, MethodProcessor>();

    @Inject
    public ModuleProcessor(Provider<BindProcessor> bindProcessorProvider, Provider<BindProviderProcessor> bindProviderProcessorProvider, Provider<BindInterceptorProcessor> bindInterceptorProcessorProvider) {
        methodProcessorMap.put(Bind.class.getName(), bindProcessorProvider.get());
        methodProcessorMap.put(BindInterceptor.class.getName(), bindInterceptorProcessorProvider.get());
        methodProcessorMap.put(BindProvider.class.getName(), bindProviderProcessorProvider.get());
    }

    public void processMethod(ASTMethod astMethod) {

        for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {

            if (methodProcessorMap.containsKey(astAnnotation.getName())) {
                MethodProcessor methodProcessor = methodProcessorMap.get(astAnnotation.getName());

                methodProcessor.process(astMethod, astAnnotation);
            }
        }
    }
}
