package org.anroidrobotics.example.simple;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidrobotics.example.simple.TestActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class TestActivityTest {

    private TestActivity testActivity;

    @Before
    public void setup() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        testActivity = new TestActivity();
        testActivity.onCreate(null);
    }

    @Test
    public void test() {

    }
}
