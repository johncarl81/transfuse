package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.gen.VariableBuilderRepository;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.PackageClass;

import javax.inject.Inject;

/**
 * Activity related Analysis
 *
 * @author John Ericksen
 */
public class ActivityAnalysis {

    private InjectionPointFactory injectionPointFactory;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    public ActivityDescriptor analyzeElement(ASTType input, VariableBuilderRepository variableBuilderRepository) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        Layout layoutAnnotation = input.getAnnotation(Layout.class);

        ActivityDescriptor activityDescriptor = null;

        if (activityAnnotation != null) {
            activityDescriptor = new ActivityDescriptor();

            String name = input.getName();
            String packageName = name.substring(0, name.lastIndexOf('.'));

            activityDescriptor.setPackageClass(new PackageClass(packageName, activityAnnotation.value()));
            activityDescriptor.setLayout(layoutAnnotation.value());

            activityDescriptor.addInjectionPoint(
                    injectionPointFactory.buildInjectionPoint(input, variableBuilderRepository)
            );
        }
        return activityDescriptor;
    }
}
