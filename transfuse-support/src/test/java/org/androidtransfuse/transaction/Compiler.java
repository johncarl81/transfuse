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
package org.androidtransfuse.transaction;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.*;

/**
 * @author John Ericksen
 */
public class Compiler {

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final Map<String, String> source = new HashMap<String, String>();
    private final MemoryFileManager manager = new MemoryFileManager(this.compiler);

    public void add(String classname, String fileContent) {
        add(Collections.singletonMap(classname, fileContent));
    }

    public void add(Map<String, String> map) {
        source.putAll(map);
    }

    public void compile() {
        List<Source> list = new ArrayList<Source>();
        for (Map.Entry<String, String> entry : source.entrySet()) {
            list.add(new Source(entry.getKey(), JavaFileObject.Kind.SOURCE, entry.getValue()));
        }
        this.compiler.getTask(null, this.manager, null, null, null, list).call();
    }

    public byte[] getByteCode(String name) {
        return this.manager.map.get(name).toByteArray();
    }
}
