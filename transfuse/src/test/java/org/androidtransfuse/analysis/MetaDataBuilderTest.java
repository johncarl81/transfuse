package org.androidtransfuse.analysis;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.MetaData;
import org.androidtransfuse.annotations.MetaDataSet;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class MetaDataBuilderTest {

    private static final String TEST_NAME = "testName";
    private static final String TEST_RESOURCE = "testResource";
    private static final String TEST_VALUE = "testValue";

    @MetaData(name = TEST_NAME, resource = TEST_RESOURCE, value = TEST_VALUE)
    public class MetaDataTarget {
    }

    @MetaDataSet({
            @MetaData(name = TEST_NAME, resource = TEST_RESOURCE, value = TEST_VALUE),
            @MetaData(name = TEST_NAME, resource = TEST_RESOURCE, value = TEST_VALUE)
    })
    public class MetaDataSetTarget {
    }

    @Inject
    private MetaDataBuilder metaDataBuilder;
    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType metaDataTargetASTType;
    private ASTType metaDataSetTargetASTType;

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);
        metaDataBuilder = new MetaDataBuilder();

        metaDataTargetASTType = astClassFactory.getType(MetaDataTarget.class);
        metaDataSetTargetASTType = astClassFactory.getType(MetaDataSetTarget.class);

    }

    @Test
    public void testMetaData() {
        List<org.androidtransfuse.model.manifest.MetaData> metaDataList = metaDataBuilder.buildMetaData(metaDataTargetASTType);

        assertEquals(1, metaDataList.size());
        org.androidtransfuse.model.manifest.MetaData metaData = metaDataList.get(0);
        assertEquals(TEST_NAME, metaData.getName());
        assertEquals(TEST_RESOURCE, metaData.getResourceSpecification());
        assertEquals(TEST_VALUE, metaData.getValue());
    }

    @Test
    public void testMetaDataSet() {
        List<org.androidtransfuse.model.manifest.MetaData> metaDataList = metaDataBuilder.buildMetaData(metaDataSetTargetASTType);

        assertEquals(2, metaDataList.size());
        org.androidtransfuse.model.manifest.MetaData metaData = metaDataList.get(0);
        assertEquals(TEST_NAME, metaData.getName());
        assertEquals(TEST_RESOURCE, metaData.getResourceSpecification());
        assertEquals(TEST_VALUE, metaData.getValue());
        org.androidtransfuse.model.manifest.MetaData metaData2 = metaDataList.get(1);
        assertEquals(TEST_NAME, metaData2.getName());
        assertEquals(TEST_RESOURCE, metaData2.getResourceSpecification());
        assertEquals(TEST_VALUE, metaData2.getValue());
    }
}
