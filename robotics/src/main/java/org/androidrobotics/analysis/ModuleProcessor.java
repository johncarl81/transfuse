package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
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

    private interface MethodProcessor {

        void process(ASTMethod astMethod, ASTAnnotation astAnnotation);
    }

    private static abstract class ClassBindingMethodProcessorAdaptor implements MethodProcessor {

        @Override
        public void process(ASTMethod astMethod, ASTAnnotation astAnnotation) {
            ASTType returnType = astMethod.getReturnType();

            ASTType astType = astAnnotation.getProperty("value", ASTType.class);

            innerProcess(returnType, astType);

        }

        public abstract void innerProcess(ASTType returnType, ASTType annotationValue);

    }

    private static final class BindProcessor extends ClassBindingMethodProcessorAdaptor {

        private InjectionNodeBuilderRepository injectionNodeBuilders;
        private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

        private BindProcessor(VariableInjectionBuilderFactory variableInjectionBuilderFactory, InjectionNodeBuilderRepository injectionNodeBuilders) {
            this.injectionNodeBuilders = injectionNodeBuilders;
            this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        }

        @Override
        public void innerProcess(ASTType returnType, ASTType annotationValue) {
            injectionNodeBuilders.put(returnType.getName(),
                    variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(annotationValue));
        }
    }

    private static final class BindInterceptorProcessor extends ClassBindingMethodProcessorAdaptor {

        private AOPRepository aopRepository;

        private BindInterceptorProcessor(AOPRepository aopRepository) {
            this.aopRepository = aopRepository;
        }

        @Override
        public void innerProcess(ASTType returnType, ASTType annotationValue) {
            aopRepository.put(annotationValue, returnType);
        }
    }

    private static final class BindProviderProcessor extends ClassBindingMethodProcessorAdaptor {

        private InjectionNodeBuilderRepository injectionNodeBuilders;
        private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

        private BindProviderProcessor(VariableInjectionBuilderFactory variableInjectionBuilderFactory, InjectionNodeBuilderRepository injectionNodeBuilders) {
            this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
            this.injectionNodeBuilders = injectionNodeBuilders;
        }

        @Override
        public void innerProcess(ASTType returnType, ASTType annotationValue) {
            injectionNodeBuilders.put(returnType.getName(),
                    variableInjectionBuilderFactory.buildProviderInjectionNodeBuilder(annotationValue));
        }
    }
}
