package org.androidrobotics.analysis.astAnalyzer;

import com.google.inject.Provider;
import org.androidrobotics.annotations.View;
import org.androidrobotics.gen.variableBuilder.ViewVariableBuilder;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class BindingRepositoryProvider implements Provider<BindingRepository> {

    @Inject
    private ViewVariableBuilder viewVariableBuilder;

    @Override
    public BindingRepository get() {

        BindingRepository bindingRepository = new BindingRepository();

        bindingRepository.addVariableBuilder(View.class.getName(), viewVariableBuilder);

        return bindingRepository;
    }
}
