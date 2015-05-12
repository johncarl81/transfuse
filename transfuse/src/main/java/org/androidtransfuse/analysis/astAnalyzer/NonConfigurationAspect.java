/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.model.Aspect;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNodeLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * NonConfiguration Instance Aspect highlighting the fields to use in the code generation of
 * onRetainNonConfigurationInstance and getLastNonConfigurationInstance
 *
 * @author John Ericksen
 */
public class NonConfigurationAspect implements Aspect{

    private final List<FieldInjectionPoint> fields = new ArrayList<FieldInjectionPoint>();

    public void add(FieldInjectionPoint nonConfigurationField) {
        fields.add(nonConfigurationField);
    }

    public List<FieldInjectionPoint> getFields() {
        return fields;
    }

    @Override
    public void log(InjectionNodeLogger logger) {
        logger.append("NonConfigurationAspect{");
        logger.pushIndent();
        for (FieldInjectionPoint field : fields) {
            logger.append("\n");
            field.log(logger);
        }
        logger.popIndent();
        logger.append("\n}");
    }
}
