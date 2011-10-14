package org.androidrobotics.gen;

/**
 * @author John Ericksen
 */
public class PackageFileName {

    private String pkg;
    private String fileName;

    public PackageFileName(String pkg, String fileName) {
        this.pkg = pkg;
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageFileName that = (PackageFileName) o;

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

    public String getName() {
        return fileName;
    }

    public PackageFileName addDotJava() {
        return new PackageFileName(pkg, fileName + ".java");
    }

    public String toString() {
        return pkg + "." + fileName;
    }
}
