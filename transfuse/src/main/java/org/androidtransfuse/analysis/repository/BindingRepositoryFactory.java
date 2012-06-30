package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.variableBuilder.*;

import javax.inject.Inject;

/**
 * Factory to create the Binding Repository
 *
 * @author John Ericksen
 */
public class BindingRepositoryFactory {

    private ViewInjectionNodeBuilder viewVariableBuilder;
    private ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    private SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;
    private ResourceInjectionNodeBuilder resourceInjectionNodeBuilder;
    private PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder;
    private FragmentViewInjectionNodeBuilder fragmentViewInjectionNodeBuilder;

    @Inject
    public BindingRepositoryFactory(ViewInjectionNodeBuilder viewVariableBuilder,
                                    ExtraInjectionNodeBuilder extraInjectionNodeBuilder,
                                    SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder,
                                    ResourceInjectionNodeBuilder resourceInjectionNodeBuilder,
                                    PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder,
                                    FragmentViewInjectionNodeBuilder fragmentViewInjectionNodeBuilder) {
        this.viewVariableBuilder = viewVariableBuilder;
        this.extraInjectionNodeBuilder = extraInjectionNodeBuilder;
        this.systemServiceBindingInjectionNodeBuilder = systemServiceBindingInjectionNodeBuilder;
        this.resourceInjectionNodeBuilder = resourceInjectionNodeBuilder;
        this.preferenceInjectionNodeBuilder = preferenceInjectionNodeBuilder;
        this.fragmentViewInjectionNodeBuilder = fragmentViewInjectionNodeBuilder;
    }

    public void addBindingAnnotations(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {

        injectionNodeBuilderRepository.putAnnotation(Extra.class, extraInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Resource.class, resourceInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(SystemService.class, systemServiceBindingInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Preference.class, preferenceInjectionNodeBuilder);

    }

    public void addViewBindingAnnotation(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {
        injectionNodeBuilderRepository.putAnnotation(View.class, viewVariableBuilder);
    }

    public void addFragmentViewBindingAnnotation(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {
        injectionNodeBuilderRepository.putAnnotation(View.class, fragmentViewInjectionNodeBuilder);
    }
}
