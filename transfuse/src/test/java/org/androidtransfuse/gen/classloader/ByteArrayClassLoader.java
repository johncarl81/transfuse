package org.androidtransfuse.gen.classloader;

import java.util.Map;

public class ByteArrayClassLoader extends ClassLoader {

    private Map<String, byte[]> classes;

    public ByteArrayClassLoader(Map<String, byte[]> classes) {
        this.classes = classes;
    }

    public Class findClass(String name) throws ClassNotFoundException {
        if (classes.containsKey(name)) {
            byte[] classBytes = classes.get(name);
            return defineClass(name, classBytes, 0, classBytes.length);
        }

        return super.findClass(name);
    }

}
