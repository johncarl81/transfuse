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
