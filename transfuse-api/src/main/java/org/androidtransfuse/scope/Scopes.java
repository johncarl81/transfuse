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
package org.androidtransfuse.scope;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author John Ericksen
 */
public class Scopes {

    public static final String ADD_SCOPE = "addScope";
    public static final String GET_SCOPE = "getScope";

    private final ConcurrentMap<Class<? extends Annotation>, Scope> scopeMapping = new ConcurrentHashMap<Class<? extends Annotation>, Scope>();

    public Scopes addScope(Class<? extends Annotation> key, Scope scope){
        scopeMapping.put(key, scope);
        return this;
    }

    public Scope getScope(Class<? extends Annotation> key){
        return scopeMapping.get(key);
    }
}
