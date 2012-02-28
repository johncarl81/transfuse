package org.androidtransfuse.analysis.astAnalyzer;

import com.google.inject.Provider;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.annotations.Resource;
import org.androidtransfuse.annotations.SystemService;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.gen.variableBuilder.ExtraInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.ResourceInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.SystemServiceBindingInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.ViewInjectionNodeBuilder;

import javax.inject.Inject;

/**
 * Factory to create the Binding Repository
 *
 * @author John Ericksen
 */
public class BindingRepositoryProvider implements Provider<BindingRepository> {

    @Inject
    private ViewInjectionNodeBuilder viewVariableBuilder;
    @Inject
    private ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    @Inject
    private SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;
    @Inject
    private ResourceInjectionNodeBuilder resourceInjectionNodeBuilder;

    @Override
    public BindingRepository get() {

        BindingRepository bindingRepository = new BindingRepository();

        bindingRepository.addVariableBuilder(View.class, viewVariableBuilder);
        bindingRepository.addVariableBuilder(Extra.class, extraInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(Resource.class, resourceInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(SystemService.class, systemServiceBindingInjectionNodeBuilder);

        return bindingRepository;
    }
}
