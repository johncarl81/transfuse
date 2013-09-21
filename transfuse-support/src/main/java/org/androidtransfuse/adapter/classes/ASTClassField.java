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
package org.androidtransfuse.adapter.classes;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.TransfuseAdapterException;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.util.AccessibleElementPrivilegedAction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;

/**
 * Class specific AST Field
 *
 * @author John Ericksen
 */
public class ASTClassField implements ASTField {

    private final Field field;
    private final ASTType astType;
    private final ASTAccessModifier modifier;
    private final ImmutableSet<ASTAnnotation> annotations;

    public ASTClassField(Field field, ASTType astType, ASTAccessModifier modifier, ImmutableSet<ASTAnnotation> annotations) {
        this.field = field;
        this.astType = astType;
        this.modifier = modifier;
        this.annotations = annotations;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return field.getAnnotation(annotation);
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }

    @Override
    public Object getConstantValue() {
        try {
            //tricky code to access constant value from the current field
            //see PrivateConstantFieldAccessPrivilegedAction below for behaviour
            return AccessController.doPrivileged(
                    new PrivateConstantFieldAccessPrivilegedAction(field));
        } catch (NullPointerException e) {
            return null;
        } catch (PrivilegedActionException e) {
            throw new TransfuseAdapterException("PrivilegedActionException when trying to set field: " + field, e);
        }
    }

    private static final class PrivateConstantFieldAccessPrivilegedAction extends AccessibleElementPrivilegedAction<Object, Field> {

        protected PrivateConstantFieldAccessPrivilegedAction(Field accessibleObject) {
            super(accessibleObject);
        }

        @Override
        public Object run(Field classField) throws IllegalAccessException {
            return classField.get(null);
        }
    }
}
