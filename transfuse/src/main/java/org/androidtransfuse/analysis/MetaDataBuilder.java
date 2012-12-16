/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.MetaData;
import org.androidtransfuse.annotations.MetaDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class MetaDataBuilder {

    public List<org.androidtransfuse.model.manifest.MetaData> buildMetaData(ASTType astType) {

        MetaDataSet metaDataSet = astType.getAnnotation(MetaDataSet.class);
        MetaData metaData = astType.getAnnotation(MetaData.class);

        List<org.androidtransfuse.model.manifest.MetaData> convertedIntentFilters = new ArrayList<org.androidtransfuse.model.manifest.MetaData>();

        if (metaDataSet != null) {
            for (MetaData metaDataItem : metaDataSet.value()) {
                convertedIntentFilters.add(buildMetaData(metaDataItem));
            }
        }
        if (metaData != null) {
            convertedIntentFilters.add(buildMetaData(metaData));
        }

        return convertedIntentFilters;
    }

    private org.androidtransfuse.model.manifest.MetaData buildMetaData(MetaData metaDataInput) {
        org.androidtransfuse.model.manifest.MetaData metaData = new org.androidtransfuse.model.manifest.MetaData();

        metaData.setName(metaDataInput.name());
        metaData.setResourceSpecification(metaDataInput.resource());
        metaData.setValue(metaDataInput.value());

        return metaData;
    }
}
