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
package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

public class BImpl implements B {
    private C c;
    private static int constructionCounter = 0;

    @Inject
    public BImpl(C c) {
        constructionCounter++;
        this.c = c;
    }

    public C getC() {
        return c;
    }

    public static int getConstructionCounter() {
        return constructionCounter;
    }

    public static void reset(){
        constructionCounter = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof B)) return false;

        B b = (B) o;

        return !(c != null ? !c.equals(b.getC()) : b.getC() != null);
    }

    @Override
    public int hashCode() {
        return c != null ? c.hashCode() : 0;
    }
}