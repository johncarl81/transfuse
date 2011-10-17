package org.androidrobotics.model;

/**
 * @author John Ericksen
 */
public class PackageClass {

    private String pkg;
    private String fileName;

    public PackageClass(String pkg, String fileName) {
        this.pkg = pkg;
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageClass that = (PackageClass) o;

        if (!fileName.equals(that.fileName)) return false;
        if (!pkg.equals(that.pkg)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pkg.hashCode();
        result = 31 * result + fileName.hashCode();
        return result;
    }

    public String getPackage() {
        return pkg;
    }

    public String getClassName() {
        return fileName;
    }

    public String getFullyQualifiedName() {
        return pkg + "." + fileName;
    }

    public PackageClass addDotJava() {
        return new PackageClass(pkg, fileName + ".java");
    }

    public String toString() {
        return getFullyQualifiedName();
    }
}
