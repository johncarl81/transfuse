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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * @author John Ericksen
 */
public class ASTDefinedAnnotation implements ASTAnnotation {

    private final ASTType type;
    private final ImmutableMap<String, Object> properties;

    public ASTDefinedAnnotation(ASTType type, ImmutableMap<String, Object> properties) {
        this.type = type;
        this.properties = properties;
    }

    @Override
    public <T> T getProperty(String name, Class<T> type) {
        return (T) properties.get(name);
    }

    @Override
    public ASTType getASTType() {
        return type;
    }

    @Override
    public ImmutableSet<String> getPropertyNames() {
        return properties.keySet();
    }
}
