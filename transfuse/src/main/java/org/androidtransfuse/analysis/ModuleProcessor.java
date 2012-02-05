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
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.VariableBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ModuleProcessor {

    private AOPRepository aopRepository;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private Map<String, MethodProcessor> methodProcessorMap = new HashMap<String, MethodProcessor>();

    @Inject
    public ModuleProcessor(AOPRepositoryProvider aopRepositoryProvider,
                           VariableBuilderRepositoryFactory variableBuilderRepositoryProvider,
                           VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        aopRepository = aopRepositoryProvider.get();
        injectionNodeBuilders = variableBuilderRepositoryProvider.buildRepository();

        methodProcessorMap.put(Bind.class.getName(), new BindProcessor(variableInjectionBuilderFactory, injectionNodeBuilders));
        methodProcessorMap.put(BindInterceptor.class.getName(), new BindInterceptorProcessor(aopRepository));
        methodProcessorMap.put(BindProvider.class.getName(), new BindProviderProcessor(variableInjectionBuilderFactory, injectionNodeBuilders));
    }

    public void processMethod(ASTMethod astMethod) {

        for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {

            if (methodProcessorMap.containsKey(astAnnotation.getName())) {
                MethodProcessor methodProcessor = methodProcessorMap.get(astAnnotation.getName());

                methodProcessor.process(astMethod, astAnnotation);
            }
        }
    }

    public InjectionNodeBuilderRepository getInjectionNodeBuilders() {
        return injectionNodeBuilders;
    }

    public AOPRepository getAOPRepository() {
        return aopRepository;
    }
}
