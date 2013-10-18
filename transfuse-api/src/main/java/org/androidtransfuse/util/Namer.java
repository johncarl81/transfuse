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
package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public final class Namer {

    private static final String SEPARATOR = "$$";

    private StringBuilder builder = new StringBuilder();

    private Namer(String name){
        //private utility class constructor
        builder.append(name);
    }

    public static Namer name(String name){
        return new Namer(name);
    }

    public Namer append(String part) {
        builder.append(SEPARATOR);
        builder.append(part);
        return this;
    }

    public Namer append(int part) {
        builder.append(SEPARATOR);
        builder.append(part);
        return this;
    }

    public String build(){
        return builder.toString();
    }

    public String toString(){
        return build();
    }
}

