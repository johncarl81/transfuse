package org.androidtransfuse.analysis.repository;

import com.google.inject.Provider;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
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
    private ASTClassFactory astClassFactory;

    @Inject
    public BindingRepositoryProvider(ViewInjectionNodeBuilder viewVariableBuilder,
                                     ExtraInjectionNodeBuilder extraInjectionNodeBuilder,
                                     SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder,
                                     ResourceInjectionNodeBuilder resourceInjectionNodeBuilder,
                                     PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder, ASTClassFactory astClassFactory) {
        this.viewVariableBuilder = viewVariableBuilder;
        this.extraInjectionNodeBuilder = extraInjectionNodeBuilder;
        this.systemServiceBindingInjectionNodeBuilder = systemServiceBindingInjectionNodeBuilder;
        this.resourceInjectionNodeBuilder = resourceInjectionNodeBuilder;
        this.preferenceInjectionNodeBuilder = preferenceInjectionNodeBuilder;
        this.astClassFactory = astClassFactory;
    }

    @Override
    public BindingRepository get() {

        BindingRepository bindingRepository = new BindingRepository();

        bindingRepository.addVariableBuilder(astClassFactory.buildASTClassType(View.class), viewVariableBuilder);
        bindingRepository.addVariableBuilder(astClassFactory.buildASTClassType(Extra.class), extraInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(astClassFactory.buildASTClassType(Resource.class), resourceInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(astClassFactory.buildASTClassType(SystemService.class), systemServiceBindingInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(astClassFactory.buildASTClassType(Preference.class), preferenceInjectionNodeBuilder);

        return bindingRepository;
    }
}
