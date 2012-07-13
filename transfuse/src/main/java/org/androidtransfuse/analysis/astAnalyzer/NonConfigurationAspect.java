package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.model.FieldInjectionPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * NonConfiguration Instance Aspect highlighting the fields to use in the code generation of
 * onRetainNonConfigurationInstance and getLastNonConfigurationInstance
 *
 * @author John Ericksen
 */
public class NonConfigurationAspect {

    private List<FieldInjectionPoint> fields = new ArrayList<FieldInjectionPoint>();

    public void add(FieldInjectionPoint nonConfigurationField) {
        fields.add(nonConfigurationField);
    }

    public List<FieldInjectionPoint> getFields() {
        return fields;
    }
}
