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
package org.androidtransfuse.model;

import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@Bootstrap
public class InjectionSignatureTest {

    @Inject
    private ASTClassFactory astClassFactory;

    public static class NamedTargets {
        @javax.inject.Named("scheduler")
        String javaxNamed;

        @jakarta.inject.Named("scheduler")
        String jakartaNamed;
    }

    @Before
    public void setup() {
        Bootstraps.inject(this);
    }

    @Test
    public void testJavaxJakartaNamedAreEqual() {
        ASTType namedTargetsType = astClassFactory.getType(NamedTargets.class);

        ASTField javaxNamedField = findField(namedTargetsType, "javaxNamed");
        ASTField jakartaNamedField = findField(namedTargetsType, "jakartaNamed");

        ASTType stringType = astClassFactory.getType(String.class);
        InjectionSignature javaxSignature = new InjectionSignature(stringType, javaxNamedField.getAnnotations());
        InjectionSignature jakartaSignature = new InjectionSignature(stringType, jakartaNamedField.getAnnotations());

        assertEquals(javaxSignature, jakartaSignature);
        assertEquals(javaxSignature.hashCode(), jakartaSignature.hashCode());
        assertEquals(javaxSignature.buildScopeKeySignature(), jakartaSignature.buildScopeKeySignature());
    }

    private ASTField findField(ASTType type, String name) {
        for (ASTField field : type.getFields()) {
            if (name.equals(field.getName())) {
                return field;
            }
        }
        throw new AssertionError("Unable to find field: " + name);
    }
}
