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
package org.androidtransfuse.util.matcher;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.model.InjectionSignature;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
@Bootstrap(test = true)
public class MatchersTest {

    @Inject
    private ASTClassFactory astClassFactory;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        String value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotationTwo {}

    public class ZeroMatcherTarget {}

    @TestAnnotation("1")
    public class SingleMatcherTarget {}

    @TestAnnotationTwo
    @TestAnnotation("2")
    public class DoubleMatcherTarget {}

    private ASTType zeroMatcherASTType;
    private ASTType singleMatcherASTType;
    private ASTType doubleMatcherASTType;
    private InjectionSignature zeroMatcherSigniture;
    private InjectionSignature singleMatcherASTTypeSigniture;
    private InjectionSignature doubleMatcherASTTypeSigniture;
    private ASTAnnotation testAnnotation1;
    private ASTAnnotation testAnnotation2;
    private ASTAnnotation testAnnotationTwo;
    private ASTType testAnnotationType;
    private ASTType testAnnotationTwoType;

    @Before
    public void setup() {
        Bootstraps.injectTest(this);

        zeroMatcherASTType = astClassFactory.getType(ZeroMatcherTarget.class);
        singleMatcherASTType = astClassFactory.getType(SingleMatcherTarget.class);
        doubleMatcherASTType = astClassFactory.getType(DoubleMatcherTarget.class);

        zeroMatcherSigniture = buildSignature(zeroMatcherASTType);
        singleMatcherASTTypeSigniture = buildSignature(singleMatcherASTType);
        doubleMatcherASTTypeSigniture = buildSignature(doubleMatcherASTType);

        testAnnotation1 = singleMatcherASTType.getASTAnnotation(TestAnnotation.class);
        testAnnotation2 = doubleMatcherASTType.getASTAnnotation(TestAnnotation.class);
        testAnnotationTwo = doubleMatcherASTType.getASTAnnotation(TestAnnotationTwo.class);
        testAnnotationType = testAnnotation1.getASTType();
        testAnnotationTwoType = testAnnotationTwo.getASTType();
    }

    private InjectionSignature buildSignature(ASTType astType) {
        return new InjectionSignature(astType, astType.getAnnotations());
    }

    @Test
    public void testSingleTypeMatch() {
        InjectionSignatureMatcher matcher = Matchers.annotated().byType(testAnnotationType).build();

        assertFalse(matcher.matches(zeroMatcherSigniture));
        assertTrue(matcher.matches(singleMatcherASTTypeSigniture));
        assertFalse(matcher.matches(doubleMatcherASTTypeSigniture));
    }

    @Test
    public void testMultiTypeMatch() {

        InjectionSignatureMatcher matcher = Matchers.annotated().byType(testAnnotationType, testAnnotationTwoType).build();

        assertFalse(matcher.matches(zeroMatcherSigniture));
        assertFalse(matcher.matches(singleMatcherASTTypeSigniture));
        assertTrue(matcher.matches(doubleMatcherASTTypeSigniture));
    }

    @Test
    public void testAnnotationNoMatch() {
        InjectionSignatureMatcher matcher = Matchers.annotated().byType(testAnnotationTwoType).build();

        assertFalse(matcher.matches(zeroMatcherSigniture));
        assertFalse(matcher.matches(singleMatcherASTTypeSigniture));
        assertFalse(matcher.matches(doubleMatcherASTTypeSigniture));
    }

    @Test
    public void testSingleAnnotationMatch() {
        InjectionSignatureMatcher matcher = Matchers.annotated().byAnnotation(testAnnotation1).build();

        assertFalse(matcher.matches(zeroMatcherSigniture));
        assertTrue(matcher.matches(singleMatcherASTTypeSigniture));
        assertFalse(matcher.matches(doubleMatcherASTTypeSigniture));
    }

    @Test
    public void testMultiAnnotationMatch() {

        InjectionSignatureMatcher matcher = Matchers.annotated().byAnnotation(testAnnotation2, testAnnotationTwo).build();

        assertFalse(matcher.matches(zeroMatcherSigniture));
        assertFalse(matcher.matches(singleMatcherASTTypeSigniture));
        assertTrue(matcher.matches(doubleMatcherASTTypeSigniture));
    }

    @Test
    public void testMultiAnnotationNoMatch() {

        InjectionSignatureMatcher matcher = Matchers.annotated().byAnnotation(testAnnotation1, testAnnotationTwo).build();

        assertFalse(matcher.matches(zeroMatcherSigniture));
        assertFalse(matcher.matches(singleMatcherASTTypeSigniture));
        assertFalse(matcher.matches(doubleMatcherASTTypeSigniture));
    }

    @Test
    public void testTypeMatch() {
        Matcher<ASTType> matcher = Matchers.type(zeroMatcherASTType).build();

        assertTrue(matcher.matches(zeroMatcherASTType));
        assertFalse(matcher.matches(singleMatcherASTType));
        assertFalse(matcher.matches(doubleMatcherASTType));
    }

    @Test
    public void testSingleAnnotationTypeMatch() {
        InjectionSignatureMatcher matcher = Matchers.type(singleMatcherASTType).annotated().byAnnotation(testAnnotation1).build();

        assertFalse(matcher.matches(zeroMatcherSigniture));
        assertTrue(matcher.matches(singleMatcherASTTypeSigniture));
        assertFalse(matcher.matches(doubleMatcherASTTypeSigniture));
    }

    @Test
    public void testSingleAnnotationTypeNoMatch() {
        InjectionSignatureMatcher matcher = Matchers.type(doubleMatcherASTType).annotated().byAnnotation(testAnnotation1).build();

        assertFalse(matcher.matches(zeroMatcherSigniture));
        assertFalse(matcher.matches(singleMatcherASTTypeSigniture));
        assertFalse(matcher.matches(doubleMatcherASTTypeSigniture));
    }

    @Test
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // felt like making this package 100% tested
        Constructor<Matchers> privateConstructor = Matchers.class.getDeclaredConstructor();

        privateConstructor.setAccessible(true);
        Matchers matchers = privateConstructor.newInstance();

        assertNotNull(matchers);
    }
}
