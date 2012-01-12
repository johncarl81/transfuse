package org.androidrobotics.analysis.astAnalyzer;

import com.google.inject.Provider;
import org.androidrobotics.annotations.Extra;
import org.androidrobotics.annotations.SystemService;
import org.androidrobotics.annotations.View;
import org.androidrobotics.gen.variableBuilder.ExtraInjectionNodeBuilder;
import org.androidrobotics.gen.variableBuilder.SystemServiceBindingInjectionNodeBuilder;
import org.androidrobotics.gen.variableBuilder.ViewInjectionNodeBuilder;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class BindingRepositoryProvider implements Provider<BindingRepository> {

    @Inject
    private ViewInjectionNodeBuilder viewVariableBuilder;
    @Inject
    private ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    @Inject
    private SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;

    @Override
    public BindingRepository get() {

        BindingRepository bindingRepository = new BindingRepository();

        bindingRepository.addVariableBuilder(View.class.getName(), viewVariableBuilder);
        bindingRepository.addVariableBuilder(Extra.class.getName(), extraInjectionNodeBuilder);
        bindingRepository.addVariableBuilder(SystemService.class.getName(), systemServiceBindingInjectionNodeBuilder);

        return bindingRepository;
    }
}
