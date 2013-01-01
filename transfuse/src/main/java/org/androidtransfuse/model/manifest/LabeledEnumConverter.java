/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.androidtransfuse.annotations.LabeledEnum;

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
        return labeledEnum.isAssignableFrom(type);
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
