---
layout: default
title: Transfuse
---
Transfuse
=========


{% highlight java %}

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
        //loop over annotations
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
{% endhighlight %}
