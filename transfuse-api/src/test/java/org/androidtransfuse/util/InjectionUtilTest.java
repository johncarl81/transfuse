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
package org.androidtransfuse.util;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
public class InjectionUtilTest {

    private static final String TEST_VALUE = "hello";
    private InjectionUtil injectionUtil;

    @Before
    public void setup() {
        injectionUtil = InjectionUtil.getInstance();
    }

    @Test
    public void testGetField(){
        Target target = new Target();

        target.setValue(TEST_VALUE);

        String value = injectionUtil.getField(String.class, Target.class, target, "value");

        assertEquals(TEST_VALUE, value);
    }

    @Test
    public void testPrivateMethodInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.callMethod(Void.class, Target.class, target, "setPrivateValue", new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(TEST_VALUE, target.getValue());
    }

    @Test
    public void testPrivateMethodGet() {
        Target target = new Target();

        assertNull(target.getValue());
        target.setValue(TEST_VALUE);

        assertEquals(TEST_VALUE, injectionUtil.callMethod(String.class, Target.class, target, "getPrivateValue", new Class[]{}, new Object[]{}));
    }

    @Test
    public void testPrivateSuperMethodInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.callMethod(Void.class, TargetSuper.class, target, "setSuperValue", new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(TEST_VALUE, target.getSuperValue());
    }

    @Test
    public void testPrivateConstructorInjection() {
        Target target = injectionUtil.callConstructor(Target.class, new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(target.getValue(), TEST_VALUE);
    }

    @Test
    public void testPrivateFieldInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.setField(Target.class, target, "value", TEST_VALUE);

        assertEquals(TEST_VALUE, target.getValue());
    }

    @Test
    public void testPrivateSuperFieldInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.setField(TargetSuper.class, target, "superValue", TEST_VALUE);

        assertEquals(TEST_VALUE, target.getSuperValue());
    }

    @Test
    public void verifyMethodNames() throws NoSuchMethodException {

        Method getInstanceMethod = InjectionUtil.class.getMethod(InjectionUtil.GET_INSTANCE_METHOD);
        assertNotNull(getInstanceMethod);
        Method callConstructorMethod = InjectionUtil.class.getMethod(InjectionUtil.CALL_CONSTRUCTOR_METHOD, Class.class, Class[].class, Object[].class);
        assertNotNull(callConstructorMethod);
        Method callMethodMethod = InjectionUtil.class.getMethod(InjectionUtil.CALL_METHOD_METHOD, Class.class, Class.class, Object.class, String.class, Class[].class, Object[].class);
        assertNotNull(callMethodMethod);
        Method getFieldMethod = InjectionUtil.class.getMethod(InjectionUtil.GET_FIELD_METHOD, Class.class, Class.class, Object.class, String.class);
        assertNotNull(getFieldMethod);
        Method setFieldMethod = InjectionUtil.class.getMethod(InjectionUtil.SET_FIELD_METHOD, Class.class, Object.class, String.class, Object.class);
        assertNotNull(setFieldMethod);
    }
}
