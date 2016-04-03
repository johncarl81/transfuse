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
package org.androidtransfuse.adapter.compiletest;

import org.androidtransfuse.adapter.PackageClass;

import javax.annotation.processing.Processor;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class MemoryClassLoader extends ClassLoader {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final MemoryFileManager manager = new MemoryFileManager(this.compiler);

    public MemoryClassLoader(){
        super(MemoryClassLoader.class.getClassLoader());
    }

    public void add(PackageClass classname, String fileContent) {
        add(Collections.singletonMap(classname, fileContent));
    }

    public void add(Map<PackageClass, String> map) {
        this.add(map, null, null);
    }

    public Boolean add(Map<PackageClass, String> map, DiagnosticListener<? super JavaFileObject> diagnosticListener, List<? extends Processor> processors) {
        List<Source> list = new ArrayList<Source>();
        for (Map.Entry<PackageClass, String> entry : map.entrySet()) {
            list.add(new Source(entry.getKey().getCanonicalName(), JavaFileObject.Kind.SOURCE, entry.getValue()));
            if(getPackage(entry.getKey().getPackage()) == null) {
                definePackage(entry.getKey().getPackage(), null, null, null, null, null, null, null);
            }
        }
        JavaCompiler.CompilationTask task = this.compiler.getTask(null, this.manager, diagnosticListener, null, null, list);
        if(processors != null) {
            task.setProcessors(processors);
        }
        return task.call();
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        synchronized (this.manager) {
            Output compilerOutput = this.manager.map.remove(name);
            if (compilerOutput != null) {
                byte[] byteCode = compilerOutput.toByteArray();
                return defineClass(name, byteCode, 0, byteCode.length);
            }
        }
        return super.findClass(name);
    }
}
