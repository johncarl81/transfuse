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
package org.androidtransfuse.scope;

import org.androidtransfuse.annotations.TransfuseModule;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
public class ScopesUtil {
    private final static Scopes INSTANCE = new Scopes()
            .addScope(ApplicationScope.ApplicationScopeQualifier.class, new ApplicationScope())
            .addScope(Singleton.class, new org.androidtransfuse.scope.ConcurrentDoubleLockingScope())
            .addScope(TransfuseModule.class, new org.androidtransfuse.scope.ConcurrentDoubleLockingScope());

    private ScopesUtil() {
    }

    public static Scopes getInstance() {
        return INSTANCE;
    }

}
