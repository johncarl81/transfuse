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
package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

public class A {
    private B b;
    private B b2;
    private static int constructionCounter = 0;

    @Inject
    public A(B b) {
        constructionCounter++;
        this.b = b;
    }

    @Inject
    public void setB2(B b2){
        this.b2 = b2;
    }

    public B getB() {
        return b;
    }

    public static int getConstructionCounter() {
        return constructionCounter;
    }

    public static void reset(){
        constructionCounter = 0;
    }
}