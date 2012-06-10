package org.androidtransfuse.analysis.repository;

import com.google.inject.Provider;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.variableBuilder.*;

import javax.inject.Inject;

/**
 * Factory to create the Binding Repository
 *
 * @author John Ericksen
 */
public class BindingRepositoryProvider implements Provider<BindingRepository> {

    private ViewInjectionNodeBuilder viewVariableBuilder;
    private ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    private SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;
    private ResourceInjectionNodeBuilder resourceInjectionNodeBuilder;
    private PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder;

    @Inject
    public BindingRepositoryProvider(ViewInjectionNodeBuilder viewVariableBuilder,
                                     ExtraInjectionNodeBuilder extraInjectionNodeBuilder,
                                     SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder,
                                     ResourceInjectionNodeBuilder resourceInjectionNodeBuilder,
                                     PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder) {
        this.viewVariableBuilder = viewVariableBuilder;
        this.extraInjectionNodeBuilder = extraInjectionNodeBuilder;
        this.systemServiceBindingInjectionNodeBuilder = systemServiceBindingInjectionNodeBuilder;
        this.resourceInjectionNodeBuilder = resourceInjectionNodeBuilder;
        this.preferenceInjectionNodeBuilder = preferenceInjectionNodeBuilder;
    }

    @Override
    public BindingRepository get() {

        BindingRepository bindingRepository = new BindingRepository();

        bindingRepository.addVariableBuilder(View.class, viewVariableBuilder);
        bindingRepository.addVariableBuilder(Extra.class, extraInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(Resource.class, resourceInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(SystemService.class, systemServiceBindingInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(Preference.class, preferenceInjectionNodeBuilder);

        return bindingRepository;
    }
}
