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
package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.inject.LibraryDependency3;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.androidtransfuse.integrationTest.IntegrationModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Provider;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml")
public class InjectionTest {

    private Injection injection;

    @Before
    public void setup() {
        InjectionActivity injectionActivity = Robolectric.buildActivity(InjectionActivity.class).create().get();

        injection = DelegateUtil.getDelegate(injectionActivity, Injection.class);
    }

    @Test
    public void testStaticInjection() {
        assertNotNull(injection.getStaticInjectionProvider());
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
    public void testProviderInjection() {
        ProvidedInjectTarget providedInjectTarget = injection.getProvidedInjectTarget();
        assertNotNull(providedInjectTarget);
        assertNotNull(providedInjectTarget.getInjectTarget());
    }

    @Test
    public void testDeclaredProviderInjection() {
        Provider<ProvidedInjectTarget> providedInjectTargetProvider = injection.getProvidedInjectTargetProvider();
        assertNotNull(providedInjectTargetProvider);
        assertNotNull(providedInjectTargetProvider.get());
    }

    @Test
    public void testGeneratedProviderInjection() {
        Provider<InjectTarget> injectTargetProvider = injection.getGeneratedProvider();
        assertNotNull(injectTargetProvider);
        InjectTarget injectTargetOne = injectTargetProvider.get();
        assertNotNull(injectTargetOne);
        InjectTarget injectTargetTwo = injectTargetProvider.get();
        assertNotNull(injectTargetTwo);
        assertNotSame(injectTargetOne, injectTargetTwo);
    }

    @Test
    public void testBaseClassInjection() {
        assertNotNull(injection.getBaseTarget());
    }

    @Test
    public void testLibraryDependency() {
        assertNotNull(injection.getLibraryDependency());
        assertNotNull(injection.getLibraryDependency().getLibraryDependency2());
    }

    @Test
    public void testGenericInjections() {
        assertNotNull(injection.getGenericInjection());
        assertNotNull(injection.getGenericInjection2());
        assertNotSame(injection.getGenericInjection().getClass(), injection.getGenericInjection2().getClass());
    }

    @Test
    public void testImplementedByInjection() {
        assertNotNull(injection.getImplBy());
        assertEquals(ImplByImpl.class, injection.getImplBy().getClass());
    }

    @Test
    public void testInnerClassInjection() {
        assertNotNull(injection.getInnerClass().getActivity());
    }

    @Test
    public void testQualifierInjection(){
        assertEquals(IntegrationModule.ONE, injection.getQualifiedOne());
        assertEquals(IntegrationModule.TWO, injection.getQualifiedTwo());
        assertEquals(IntegrationModule.THREE, injection.getQualifiedThree());
        assertEquals(IntegrationModule.FOUR, injection.getQualifiedFour());
        assertEquals(IntegrationModule.FIVE, injection.getQualifierFive());
    }

    @Test
    public void testDependencyModuleOverride(){
        assertEquals(LibraryDependencyOverride.class, injection.getLibraryDependency().getLibraryDependency().getClass());
        assertEquals(LibraryDependency3.class, injection.getLibraryDependency().getLibraryDependency3().getClass());
    }
}
