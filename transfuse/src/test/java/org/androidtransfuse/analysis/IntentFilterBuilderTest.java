package org.androidtransfuse.analysis;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Intent;
import org.androidtransfuse.annotations.IntentFilter;
import org.androidtransfuse.annotations.IntentFilters;
import org.androidtransfuse.annotations.IntentType;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class IntentFilterBuilderTest {

    protected static final String TEST_INTENT = "test";

    @Intent(type = IntentType.ACTION, name = IntentFilterBuilderTest.TEST_INTENT)
    public class IntentTarget {}

    @IntentFilter({
            @Intent(type = IntentType.ACTION, name = IntentFilterBuilderTest.TEST_INTENT),
            @Intent(type = IntentType.CATEGORY, name = IntentFilterBuilderTest.TEST_INTENT)
    })
    public class IntentFilerTarget{}


    @IntentFilters ({
            @IntentFilter(
                @Intent(type = IntentType.ACTION, name = IntentFilterBuilderTest.TEST_INTENT)
            ),
            @IntentFilter(
                    @Intent(type = IntentType.CATEGORY, name = IntentFilterBuilderTest.TEST_INTENT)
            )
    })
    public class IntentFiltersTarget {}

    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private IntentFilterBuilder intentFilterBuilder;

    @Before
    public void setup(){
        TransfuseTestInjector.inject(this);
    }

    @Test
    public void testIntent(){
        ASTType intentAstType = astClassFactory.buildASTClassType(IntentTarget.class);

        List<org.androidtransfuse.model.manifest.IntentFilter> intentFilters = intentFilterBuilder.buildIntentFilters(intentAstType);

        assertEquals(1, intentFilters.size());
        assertEquals(TEST_INTENT, intentFilters.get(0).getActions().get(0).getName());
    }

    @Test
    public void testIntentFilter(){
        ASTType intentAstType = astClassFactory.buildASTClassType(IntentFilerTarget.class);

        List<org.androidtransfuse.model.manifest.IntentFilter> intentFilters = intentFilterBuilder.buildIntentFilters(intentAstType);

        assertEquals(1, intentFilters.size());
        assertEquals(TEST_INTENT, intentFilters.get(0).getActions().get(0).getName());
        assertEquals(TEST_INTENT, intentFilters.get(0).getCategories().get(0).getName());
    }

    @Test
    public void testIntentFilters(){
        ASTType intentAstType = astClassFactory.buildASTClassType(IntentFiltersTarget.class);

        List<org.androidtransfuse.model.manifest.IntentFilter> intentFilters = intentFilterBuilder.buildIntentFilters(intentAstType);

        assertEquals(2, intentFilters.size());
        assertEquals(TEST_INTENT, intentFilters.get(0).getActions().get(0).getName());
        assertEquals(TEST_INTENT, intentFilters.get(1).getCategories().get(0).getName());
    }
}
