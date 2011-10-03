package org.androidrobotics.analysis;

import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.gen.ActivityDescriptor;

import java.lang.reflect.AnnotatedElement;

/**
 * @author John Ericksen
 */
public class ActivityAnalysis implements RoboticsAnalysis<ActivityDescriptor> {
    @Override
    public ActivityDescriptor analyzeElement(AnnotatedElement input) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        Layout layoutAnnotation = input.getAnnotation(Layout.class);

        ActivityDescriptor activityDescriptor = null;

        if (activityAnnotation != null) {
            activityDescriptor = new ActivityDescriptor();

            activityDescriptor.setName(activityAnnotation.value());
            activityDescriptor.setLayout(layoutAnnotation.value());


        }
        return activityDescriptor;
    }

    @Override
    public Class<? extends ActivityDescriptor> getTargetDescriptor() {
        return ActivityDescriptor.class;
    }
}
