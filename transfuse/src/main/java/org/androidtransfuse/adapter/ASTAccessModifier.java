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
package org.androidtransfuse.adapter;

import com.sun.codemodel.JMod;

import java.lang.reflect.Modifier;

/**
 * Enumeration of available access modifiers, associated with codeModel modifier types
 *
 * @author John Ericksen
 */
public enum ASTAccessModifier {

    PUBLIC(JMod.PUBLIC, Modifier.PUBLIC) {
        @Override
        public boolean isModifier(int modifier) {
            return Modifier.isPublic(modifier);
        }
    },
    PROTECTED(JMod.PROTECTED, Modifier.PROTECTED) {
        @Override
        public boolean isModifier(int modifier) {
            return Modifier.isProtected(modifier);
        }
    },
    PACKAGE_PRIVATE(JMod.NONE, 0) {
        @Override
        public boolean isModifier(int modifier) {
            return false;
        }
    },
    PRIVATE(JMod.PRIVATE, Modifier.PRIVATE) {
        @Override
        public boolean isModifier(int modifier) {
            return Modifier.isPrivate(modifier);
        }
    };

    private final int codeModelJMod;
    private final int javaModifier;

    private ASTAccessModifier(int codeModelJMod, int javaModifier) {
        this.codeModelJMod = codeModelJMod;
        this.javaModifier = javaModifier;
    }

    public int getCodeModelJMod() {
        return codeModelJMod;
    }

    public int getJavaModifier() {
        return javaModifier;
    }

    public static ASTAccessModifier getModifier(int modifier) {
        for (ASTAccessModifier astAccessModifier : values()) {
            if (astAccessModifier.isModifier(modifier)) {
                return astAccessModifier;
            }
        }
        return PACKAGE_PRIVATE;
    }

    public abstract boolean isModifier(int modifier);
}
