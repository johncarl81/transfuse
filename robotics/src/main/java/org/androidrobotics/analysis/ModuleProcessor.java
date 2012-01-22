package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.module.BindInterceptorProcessor;
import org.androidrobotics.analysis.module.BindProcessor;
import org.androidrobotics.analysis.module.BindProviderProcessor;
import org.androidrobotics.analysis.module.MethodProcessor;
import org.androidrobotics.annotations.Bind;
import org.androidrobotics.annotations.BindInterceptor;
import org.androidrobotics.annotations.BindProvider;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.gen.VariableBuilderRepositoryFactory;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;

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
