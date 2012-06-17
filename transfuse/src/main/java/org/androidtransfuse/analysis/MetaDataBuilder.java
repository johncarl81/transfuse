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
