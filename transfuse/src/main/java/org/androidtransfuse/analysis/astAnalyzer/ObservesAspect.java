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
 * @author John Ericksen
 */
public class ObservesAspect implements Aspect {

    private final Map<ASTType, Set<ASTMethod>> observesMap = new HashMap<ASTType, Set<ASTMethod>>();

    public void addObserver(ASTType event, ASTMethod method){
        if(!observesMap.containsKey(event)){
            observesMap.put(event, new HashSet<ASTMethod>());
        }

        observesMap.get(event).add(new ASTMethodUniqueSignatureDecorator(method));
    }

    public Set<ASTType> getEvents(){
        return observesMap.keySet();
    }

    public Set<ASTMethod> getObserverMethods(ASTType event){
        if(observesMap.containsKey(event)){
            return observesMap.get(event);
        }

        return Collections.emptySet();
    }

    @Override
    public void log(InjectionNodeLogger logger) {
        logger.append("ObservesAspect{");
        logger.pushIndent();
        for (Map.Entry<ASTType, Set<ASTMethod>> field : observesMap.entrySet()) {
            logger.append("\n");
            logger.append(field.getKey()).append(" -> (").append(Joiner.on(", ").join(field.getValue())).append(")");
        }
        logger.popIndent();
        logger.append("\n}");
    }
}
