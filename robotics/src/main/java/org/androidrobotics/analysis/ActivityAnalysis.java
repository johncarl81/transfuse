package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.model.ActivityDescriptor;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ActivityAnalysis implements RoboticsAnalysis<ActivityDescriptor> {

    private InjectionPointFactory injectionPointFactory;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public ActivityDescriptor analyzeElement(ASTType input) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        Layout layoutAnnotation = input.getAnnotation(Layout.class);

        ActivityDescriptor activityDescriptor = null;

        if (activityAnnotation != null) {
            activityDescriptor = new ActivityDescriptor();

            activityDescriptor.setName(activityAnnotation.value());
            activityDescriptor.setLayout(layoutAnnotation.value());

            activityDescriptor.addInjectionPoint(
                    injectionPointFactory.buildInjectionPoint(input)
            );

            String name = input.getName();
            int lastDot = name.lastIndexOf('.');

            //activityDescriptor.setDelegateClass(input.getName().substring(lastDot + 1));
            activityDescriptor.setPackage(input.getName().substring(0, lastDot));

            //scan enclosed elements
            /*for (AnalysisBridge enclosedElement : input.getEnclosedElements()) {
                if (enclosedElement.getType() == ElementKind.METHOD && enclosedElement.getAnnotation(OnCreate.class) != null) {
                    activityDescriptor.addMethod(OnCreate.class, enclosedElement.getName());
                }

            }*/
        }
        return activityDescriptor;
    }

    @Override
    public Class<? extends ActivityDescriptor> getTargetDescriptor() {
        return ActivityDescriptor.class;
    }
}
