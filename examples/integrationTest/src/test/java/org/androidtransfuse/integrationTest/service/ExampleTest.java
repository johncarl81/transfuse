package org.androidtransfuse.integrationTest.service;

import android.content.Intent;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ExampleTest {

    private Example example;
    private ExampleService exampleService;

    @Before
    public void setup() {
        exampleService = new ExampleService();
        exampleService.onCreate();

        example = DelegateUtil.getDelegate(exampleService, Example.class);
    }

    @Test
    public void testOnCreate() {
        Assert.assertTrue(example.isOnCreateCalled());
    }

    @Test
    public void testOnStart() {
        Intent intent = new Intent();
        assertFalse(example.isOnStartCalled());
        exampleService.onStart(intent, 1);
        assertTrue(example.isOnStartCalled());
    }

    @Test
    public void testOnDestroy() {
        assertFalse(example.isOnDestroyCalled());
        exampleService.onDestroy();
        assertTrue(example.isOnDestroyCalled());
    }

    @Test
    public void testOnLowMemory() {
        assertFalse(example.isOnLowMemoryCalled());
        exampleService.onLowMemory();
        assertTrue(example.isOnLowMemoryCalled());
    }

    @Test
    public void testOnRebind() {
        Intent intent = new Intent();
        assertFalse(example.isOnRebindCalled());
        exampleService.onRebind(intent);
        assertTrue(example.isOnRebindCalled());
    }
}
