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
package org.androidtransfuse.analysis.astAnalyzer;

import com.google.common.base.Joiner;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTMethodUniqueSignatureDecorator;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.model.Aspect;
import org.androidtransfuse.model.InjectionNodeLogger;

import java.util.*;

/**
 * Aspect associating listenable methods with the set of listening methods.
 *
 * @author John Ericksen
 */
public class ListenerAspect implements Aspect {

    private final Map<ASTType, Set<ASTMethod>> listeners = new HashMap<ASTType, Set<ASTMethod>>();

    public void addMethodCallback(ASTType annotationType, ASTMethod method) {
        if (!listeners.containsKey(annotationType)) {
            listeners.put(annotationType, new HashSet<ASTMethod>());
        }
        Set<ASTMethod> methods = listeners.get(annotationType);

        methods.add(new ASTMethodUniqueSignatureDecorator(method));
    }

    public Set<ASTMethod> getListeners(ASTType annotationType) {
        if(listeners.containsKey(annotationType)){
            return listeners.get(annotationType);
        }

        return Collections.emptySet();
    }

    public boolean contains(ASTType annotationType) {
        return listeners.containsKey(annotationType);
    }

    @Override
    public void log(InjectionNodeLogger logger) {
        logger.append("Listeners{");
        logger.pushIndent();
        for (Map.Entry<ASTType, Set<ASTMethod>> entry : listeners.entrySet()) {
            logger.append(entry.getKey() + " -> " + "(" + Joiner.on(", ").join(entry.getValue()) + ")");
        }
        logger.popIndent();
        logger.append("\n}");
    }
}
