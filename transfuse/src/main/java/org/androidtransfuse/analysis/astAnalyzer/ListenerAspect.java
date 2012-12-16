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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTMethodUniqueSignatureDecorator;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Aspect associating listenable methods with the set of listening methods.
 *
 * @author John Ericksen
 */
public class ListenerAspect {

    private final Map<Class<? extends Annotation>, Set<ASTMethod>> listeners = new HashMap<Class<? extends Annotation>, Set<ASTMethod>>();

    public void addMethodCallback(Class<? extends Annotation> annotation, ASTMethod method) {
        if (!listeners.containsKey(annotation)) {
            listeners.put(annotation, new HashSet<ASTMethod>());
        }
        Set<ASTMethod> methods = listeners.get(annotation);

        methods.add(new ASTMethodUniqueSignatureDecorator(method));
    }

    public Set<ASTMethod> getListeners(Class<? extends Annotation> annotation) {
        if(listeners.containsKey(annotation)){
            return listeners.get(annotation);
        }

        return Collections.emptySet();
    }

    public boolean contains(Class<? extends Annotation> annotation) {
        return listeners.containsKey(annotation);
    }
}
