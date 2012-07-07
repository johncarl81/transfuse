package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.model.FieldInjectionPoint;

import java.util.ArrayList;
import java.util.List;

/**
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
