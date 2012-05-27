package org.androidtransfuse.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
public class PackageClass {

    private static final String DOT_JAVA = ".java";

    private String pkg;
    private String fileName;
    private boolean dotJava;

    public PackageClass(String fullyQualifiedName) {
        int dotIndex = fullyQualifiedName.lastIndexOf('.');
        if (dotIndex == -1) {
            fileName = fullyQualifiedName;
        } else {
            pkg = fullyQualifiedName.substring(0, dotIndex);
            fileName = fullyQualifiedName.substring(dotIndex + 1);
        }
    }

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
    public boolean equals(Object obj) {
        if (!(obj instanceof PackageClass)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        PackageClass rhs = (PackageClass) obj;
        return new EqualsBuilder()
                .append(fileName, rhs.fileName)
                .append(pkg, rhs.pkg)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(fileName).append(fileName).hashCode();
    }

    public PackageClass replaceName(String replacement) {
        return new PackageClass(pkg, replacement);
    }

    public PackageClass appendName(String append) {
        return new PackageClass(pkg, fileName + append);
    }
}
