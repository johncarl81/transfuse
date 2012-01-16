package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class LabeledEnumConverter<T extends LabeledEnum> extends AbstractSingleValueConverter {

    private Class<T> labeledEnum;
    private Map<String, T> labelMap;

    public LabeledEnumConverter(Class<T> labeledEnum, T[] values) {
        this.labeledEnum = labeledEnum;
        labelMap = new HashMap<String, T>();
        for (T value : values) {
            labelMap.put(value.getLabel(), value);
        }
    }

    @Override
    public boolean canConvert(Class type) {
        return type == labeledEnum;
    }

    @Override
    public Object fromString(String label) {
        return labelMap.get(label);
    }

    @Override
    public String toString(Object obj) {
        return labeledEnum.cast(obj).getLabel();
    }
}
