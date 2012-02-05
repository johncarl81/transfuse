package org.androidtransfuse.model;

/**
 * @author John Ericksen
 */
public class PackageClass {

    private static final String DOT_JAVA = ".java";

    private String pkg;
    private String fileName;
    private boolean dotJava;

    public PackageClass(String pkg, String fileName) {
        this(pkg, fileName, false);
    }

    private PackageClass(String pkg, String fileName, boolean dotJava) {
        this.pkg = pkg;
        if (fileName.endsWith(DOT_JAVA)) {
            this.fileName = fileName.substring(0, fileName.length() - DOT_JAVA.length());
            this.dotJava = true;
        } else {
            this.fileName = fileName;
            this.dotJava = dotJava;
        }
    }

    public PackageClass(Class<?> injectionTargetClass) {
        this(injectionTargetClass.getPackage().getName(), injectionTargetClass.getSimpleName());
    }

    public String getPackage() {
        return pkg;
    }

    public String getClassName() {
        return fileName + (dotJava ? DOT_JAVA : "");
    }

    public String getFullyQualifiedName() {
        return pkg + "." + getClassName();
    }

    public PackageClass addDotJava() {
        return new PackageClass(pkg, fileName, true);
    }

    public PackageClass removeDotJava() {
        return new PackageClass(pkg, fileName, false);
    }

    public PackageClass add(String addName) {
        return new PackageClass(pkg, fileName + addName, dotJava);
    }

    public String toString() {
        return getFullyQualifiedName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PackageClass that = (PackageClass) o;

        if (!fileName.equals(that.fileName)) {
            return false;
        }
        if (!pkg.equals(that.pkg)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = pkg.hashCode();
        result = 31 * result + fileName.hashCode();
        return result;
    }
}
