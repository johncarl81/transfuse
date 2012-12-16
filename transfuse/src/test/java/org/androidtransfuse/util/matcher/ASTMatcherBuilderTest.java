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
package org.androidtransfuse.util.matcher;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class ASTMatcherBuilderTest {

    @Inject
    private ASTMatcherBuilder matcherBuilder;
    @Inject
    private ASTClassFactory astClassFactory;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotationTwo {
    }

    public class ZeroMatcherTarget {
    }

    @TestAnnotation
    public class SingleMatcherTarget {
    }

    @TestAnnotationTwo
    @TestAnnotation
    public class DoubleMatcherTarget {
    }

    private ASTType zeroMatcherASTType;
    private ASTType singleMatcherASTType;
    private ASTType doubleMatcherASTType;

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);

        zeroMatcherASTType = astClassFactory.getType(ZeroMatcherTarget.class);
        singleMatcherASTType = astClassFactory.getType(SingleMatcherTarget.class);
        doubleMatcherASTType = astClassFactory.getType(DoubleMatcherTarget.class);
    }

    @Test
    public void testZeroMatch() {
        ASTTypeMatcherBuilder typeMatcher = matcherBuilder.type();

        //zero matches

        Matcher<ASTType> matcher = typeMatcher.build();

        assertTrue(matcher.matches(zeroMatcherASTType));
        assertTrue(matcher.matches(singleMatcherASTType));
        assertTrue(matcher.matches(doubleMatcherASTType));
    }

    @Test
    public void testMatch() {
        ASTTypeMatcherBuilder typeMatcher = matcherBuilder.type();

        typeMatcher.annotatedWith(TestAnnotation.class);

        Matcher<ASTType> matcher = typeMatcher.build();

        assertFalse(matcher.matches(zeroMatcherASTType));
        assertTrue(matcher.matches(singleMatcherASTType));
        assertTrue(matcher.matches(doubleMatcherASTType));
    }

    @Test
    public void testMultiMatch() {
        ASTTypeMatcherBuilder typeMatcher = matcherBuilder.type();

        typeMatcher.annotatedWith(TestAnnotation.class)
                .annotatedWith(TestAnnotationTwo.class);

        Matcher<ASTType> matcher = typeMatcher.build();

        assertFalse(matcher.matches(zeroMatcherASTType));
        assertFalse(matcher.matches(singleMatcherASTType));
        assertTrue(matcher.matches(doubleMatcherASTType));
    }
}
