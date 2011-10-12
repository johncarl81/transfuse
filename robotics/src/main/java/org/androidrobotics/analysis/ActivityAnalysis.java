package org.androidrobotics.analysis;

import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.annotations.OnCreate;
import org.androidrobotics.gen.ActivityDescriptor;

import javax.lang.model.element.ElementKind;

/**
 * @author John Ericksen
 */
public class ActivityAnalysis implements RoboticsAnalysis<ActivityDescriptor> {
    @Override
    public ActivityDescriptor analyzeElement(TypeAnalysisBridge input) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        Layout layoutAnnotation = input.getAnnotation(Layout.class);

        ActivityDescriptor activityDescriptor = null;

        if (activityAnnotation != null) {
            activityDescriptor = new ActivityDescriptor();

            activityDescriptor.setDelegateAnalysis(input);

            activityDescriptor.setName(activityAnnotation.value());
            activityDescriptor.setLayout(layoutAnnotation.value());

            String name = input.getName();
            int lastDot = name.lastIndexOf('.');

            activityDescriptor.setDelegateClass(input.getName().substring(lastDot + 1));
            activityDescriptor.setPackage(input.getName().substring(0, lastDot));

            //scan enclosed elements
            for (AnalysisBridge enclosedElement : input.getEnclosedElements()) {
                if (enclosedElement.getType() == ElementKind.METHOD && enclosedElement.getAnnotation(OnCreate.class) != null) {
                    activityDescriptor.addMethod(OnCreate.class, enclosedElement.getName());
                }

            }
        }
        return activityDescriptor;
    }

    @Override
    public Class<? extends ActivityDescriptor> getTargetDescriptor() {
        return ActivityDescriptor.class;
    }
}
