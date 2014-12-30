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
package org.androidtransfuse.adapter.element;

import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor6;

/**
 * Adapter to allow the implementing class to not define certain visitors, defaulted to noop behaviour.
 *
 * @author John Ericksen
 */
public class ElementVisitorAdaptor<T, R> extends SimpleElementVisitor6<T, R> {

    @Override
    public T visitPackage(PackageElement packageElement, R r) {
        return null;
    }

    @Override
    public T visitType(TypeElement typeElement, R r) {
        return null;
    }

    @Override
    public T visitVariable(VariableElement variableElement, R r) {
        return null;
    }

    @Override
    public T visitExecutable(ExecutableElement executableElement, R r) {
        return null;
    }

    @Override
    public T visitTypeParameter(TypeParameterElement typeParameterElement, R r) {
        return null;
    }

    @Override
    public T visitUnknown(Element element, R r) {
        return null;
    }
}
