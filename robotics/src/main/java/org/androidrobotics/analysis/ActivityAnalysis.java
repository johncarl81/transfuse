package org.androidrobotics.analysis;

import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.annotations.OnCreate;
import org.androidrobotics.gen.ActivityDescriptor;

/**
 * @author John Ericksen
 */
public class ActivityAnalysis implements RoboticsAnalysis<ActivityDescriptor> {
    @Override
    public ActivityDescriptor analyzeElement(AnalysisBridge input) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        Layout layoutAnnotation = input.getAnnotation(Layout.class);

        ActivityDescriptor activityDescriptor = null;

        if (activityAnnotation != null) {
            activityDescriptor = new ActivityDescriptor();

            activityDescriptor.setName(activityAnnotation.value());
            activityDescriptor.setLayout(layoutAnnotation.value());
            activityDescriptor.setDelegateClass(input.getName());

            //scan enclosed elements
            for (AnalysisBridge enclosedElement : input.getEnclosedElements()) {
                if (enclosedElement.getAnnotation(OnCreate.class) != null) {
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
