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
package org.androidtransfuse.experiment;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * @author John Ericksen
 */
public enum GenerationPhase {

    SUPER(InjectionPhase.PRE),
    INIT(InjectionPhase.PRE),
    LAYOUT(InjectionPhase.PRE),
    SCOPES(InjectionPhase.PRE),
    INJECTION(InjectionPhase.INJECTION),
    REGISTRATION(InjectionPhase.POST),
    EVENT(InjectionPhase.POST),
    RETURN(InjectionPhase.POST);

    public enum InjectionPhase{
        PRE,
        INJECTION,
        POST
    }

    private static final EnumMap<InjectionPhase, List<GenerationPhase>> INJECTION_PHASES = new EnumMap<InjectionPhase, List<GenerationPhase>>(InjectionPhase.class);
    private final InjectionPhase injectionPhase;

    static{
        for (InjectionPhase injectionPhase : InjectionPhase.values()) {
            INJECTION_PHASES.put(injectionPhase, new ArrayList<GenerationPhase>());
        }
        for (GenerationPhase generationPhase : values()) {
            INJECTION_PHASES.get(generationPhase.injectionPhase).add(generationPhase);
        }
    }

    GenerationPhase(InjectionPhase injectionPhase) {
        this.injectionPhase = injectionPhase;
    }

    public static List<GenerationPhase>  preInjectionPhases() {
        return INJECTION_PHASES.get(InjectionPhase.PRE);
    }

    public static List<GenerationPhase> postInjectionPhases() {
        return INJECTION_PHASES.get(InjectionPhase.POST);
    }
}
