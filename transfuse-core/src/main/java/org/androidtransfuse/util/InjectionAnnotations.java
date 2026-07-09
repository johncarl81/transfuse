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
package org.androidtransfuse.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.adapter.ASTStringType;
import org.androidtransfuse.adapter.ASTType;

/**
 * Canonical references for the JSR-330 ({@code javax.inject}) and Jakarta ({@code jakarta.inject})
 * injection annotations. Transfuse recognizes both namespaces in user code.
 *
 * Annotation matching in the AST layer is name based (see ASTEmptyType.equals), so each annotation
 * is represented here by an ASTStringType. This deliberately avoids a compile time dependency on
 * either namespace: the jakarta.inject artifact need not be present on the processor classpath for
 * jakarta annotations to be detected.
 *
 * @author John Ericksen
 */
public final class InjectionAnnotations {

    public static final ASTType JAVAX_INJECT = new ASTStringType("javax.inject.Inject");
    public static final ASTType JAKARTA_INJECT = new ASTStringType("jakarta.inject.Inject");
    public static final ImmutableSet<ASTType> INJECT = ImmutableSet.of(JAVAX_INJECT, JAKARTA_INJECT);

    public static final ASTType JAVAX_QUALIFIER = new ASTStringType("javax.inject.Qualifier");
    public static final ASTType JAKARTA_QUALIFIER = new ASTStringType("jakarta.inject.Qualifier");
    public static final ImmutableSet<ASTType> QUALIFIER = ImmutableSet.of(JAVAX_QUALIFIER, JAKARTA_QUALIFIER);

    public static final ASTType JAVAX_SCOPE = new ASTStringType("javax.inject.Scope");
    public static final ASTType JAKARTA_SCOPE = new ASTStringType("jakarta.inject.Scope");
    public static final ImmutableSet<ASTType> SCOPE = ImmutableSet.of(JAVAX_SCOPE, JAKARTA_SCOPE);

    public static final ASTType JAVAX_SINGLETON = new ASTStringType("javax.inject.Singleton");
    public static final ASTType JAKARTA_SINGLETON = new ASTStringType("jakarta.inject.Singleton");
    public static final ImmutableSet<ASTType> SINGLETON = ImmutableSet.of(JAVAX_SINGLETON, JAKARTA_SINGLETON);

    public static final ASTType JAVAX_NAMED = new ASTStringType("javax.inject.Named");
    public static final ASTType JAKARTA_NAMED = new ASTStringType("jakarta.inject.Named");
    public static final ImmutableSet<ASTType> NAMED = ImmutableSet.of(JAVAX_NAMED, JAKARTA_NAMED);

    public static final ASTType JAVAX_PROVIDER = new ASTStringType("javax.inject.Provider");
    public static final ASTType JAKARTA_PROVIDER = new ASTStringType("jakarta.inject.Provider");
    public static final ImmutableSet<ASTType> PROVIDER = ImmutableSet.of(JAVAX_PROVIDER, JAKARTA_PROVIDER);

    private static final ImmutableMap<String, ASTType> JAKARTA_TO_JAVAX = ImmutableMap.<String, ASTType>builder()
            .put(JAKARTA_INJECT.getName(), JAVAX_INJECT)
            .put(JAKARTA_QUALIFIER.getName(), JAVAX_QUALIFIER)
            .put(JAKARTA_SCOPE.getName(), JAVAX_SCOPE)
            .put(JAKARTA_SINGLETON.getName(), JAVAX_SINGLETON)
            .put(JAKARTA_NAMED.getName(), JAVAX_NAMED)
            .put(JAKARTA_PROVIDER.getName(), JAVAX_PROVIDER)
            .build();

    private InjectionAnnotations() {
        //private utility constructor
    }

    /**
     * Determines whether the given target is annotated with any of the supplied annotation types.
     *
     * @param target          the element under inspection
     * @param annotationTypes the accepted annotation types (typically one of the sets above)
     * @return true if the target carries any of the annotation types
     */
    public static boolean isAnnotated(ASTBase target, ImmutableSet<ASTType> annotationTypes) {
        for (ASTType annotationType : annotationTypes) {
            if (target.isAnnotated(annotationType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the given type is one of the injection {@code Provider} types.
     *
     * @param astType the type under inspection
     * @return true if the type is a javax or jakarta Provider
     */
    public static boolean isProvider(ASTType astType) {
        return PROVIDER.contains(astType);
    }

    /**
     * Resolves the {@code Provider} type (javax or jakarta) that a generated provider should implement,
     * matching the namespace of the declared injection point so the generated code assigns cleanly.
     *
     * @param declaredProviderType the declared Provider type at the injection point (e.g. {@code jakarta.inject.Provider<Foo>})
     * @return the raw Provider type to emit; defaults to {@link #JAVAX_PROVIDER}
     */
    public static ASTType providerTypeFor(ASTType declaredProviderType) {
        if (JAKARTA_PROVIDER.getName().equals(declaredProviderType.getName())) {
            return JAKARTA_PROVIDER;
        }
        return JAVAX_PROVIDER;
    }

    /**
     * Names a jakarta injection annotation under its javax equivalent, leaving every other annotation
     * type untouched. A {@code jakarta.inject.Named("a")} qualifier names the same injection point as
     * {@code javax.inject.Named("a")}, so the two must reduce to a single identity.
     *
     * Only the name is canonicalized. Substituting the annotation's ASTType would discard its
     * meta-annotations, and qualifier detection reads those (see AnnotatedPredicate).
     *
     * @param annotationType the annotation type under inspection
     * @return the javax equivalent's name, or the type's own name if it is not a jakarta annotation
     */
    public static String canonicalName(ASTType annotationType) {
        ASTType javaxEquivalent = JAKARTA_TO_JAVAX.get(annotationType.getName());
        if (javaxEquivalent == null) {
            return annotationType.getName();
        }
        return javaxEquivalent.getName();
    }
}
