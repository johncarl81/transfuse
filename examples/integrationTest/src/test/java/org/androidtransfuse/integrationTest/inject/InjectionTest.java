package org.androidtransfuse.integrationTest.inject;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class InjectionTest {

    private Injection injection;

    @Before
    public void setup() {
        InjectionActivity injectionActivity = new InjectionActivity();
        injectionActivity.onCreate(null);

        injection = DelegateUtil.getDelegate(injectionActivity, Injection.class);
    }

    @Test
    public void testFieldInjections() {
        assertNotNull(injection.getPublicInjection());
        assertNotNull(injection.getPrivateInjection());
        assertNotNull(injection.getPackagePrivateInjection());
        assertNotNull(injection.getProtectedInjection());
    }

    @Test
    public void testMethodInjection() {
        assertNotNull(injection.getMethodInjectionOne());
        assertNotNull(injection.getMethodInjectionTwo());
    }

    @Test
    public void testConstructorInjection() {
        assertNotNull(injection.getConstructorInjection());
    }

    @Test
    public void testDependencyLoop() {
        LoopOne one = injection.getDependencyLoopOne();
        assertNotNull(one);
        LoopTwo two = one.getTwo();
        assertNotNull(two);
        LoopThree three = two.getThree();
        assertNotNull(three);
        LoopOne oneLoop = three.getOne();
        assertNotNull(oneLoop);
    }
    
    @Test
    public void testProviderInjection(){
        ProvidedInjectTarget providedInjectTarget = injection.getProvidedInjectTarget();
        assertNotNull(providedInjectTarget);
        assertNotNull(providedInjectTarget.getInjectTarget());
    }
}
