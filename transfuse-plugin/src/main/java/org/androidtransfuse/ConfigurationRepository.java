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
package org.androidtransfuse;

import org.androidtransfuse.adapter.ASTType;

import java.lang.annotation.Annotation;

/**
 * @author John Ericksen
 */
public interface ConfigurationRepository {

    ComponentBuilder component(Class<? extends Annotation> annotation);

    void addEvent(Class<? extends Annotation> componentType, ASTType type, EventMapping eventMapping);

    void addMapping(Class<? extends Annotation> componentType, ASTType type, InjectionMapping eventMapping);

    void addCallThroughEvent(Class<? extends Annotation> componentType, ASTType type, Class<?> callThroughEventClass);

    void addListener(Class<? extends Annotation> componentType, ASTType type, ASTType listenerType, String listenerMethod);
}
