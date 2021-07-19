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
package org.androidtransfuse.model.manifest;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author John Ericksen
 */
public class HexadecimalIntegerConverter extends XmlAdapter<String, Integer> {

    @Override
    public Integer unmarshal(String input) throws Exception {
        if(input == null){
            return null;
        }
        return Integer.decode(input);
    }

    @Override
    public String marshal(Integer integer) throws Exception {
        if(integer == null){
            return null;
        }
        return String.format("0x%08X", integer);
    }
}
