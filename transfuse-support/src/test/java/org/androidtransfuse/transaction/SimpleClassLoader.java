package org.androidtransfuse.transaction;

/**
 * @author John Ericksen
 */
public class SimpleClassLoader extends ClassLoader {

    private String classname;
    private byte[] byteCode;

    public SimpleClassLoader(String classname, byte[] byteCode) {
        super(SimpleClassLoader.class.getClassLoader());
        this.classname = classname;
        this.byteCode = byteCode;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(name.equals(classname)){
            definePackage("example.test", null, null, null, null, null, null, null);
            return defineClass(name, byteCode, 0, byteCode.length);
        }
        return super.findClass(name);
    }
}
