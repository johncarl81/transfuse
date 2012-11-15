package org.androidtransfuse.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Class to deal with parsing and outputting class names including package and file extension.
 *
 * @author John Ericksen
 */
public class PackageClass {

    private static final String DOT_JAVA = ".java";

    private final String pkg;
    private final String fileName;

    /**
     * Constructor taking a fully qualified class name, including optional package and filename.
     * <p/>
     * This fully qualified name must not end with .java, as this will be interpreted as the class name.
     * <p/>
     * This constructor does not work with inner classes as it assumes the last substring is the Class name and
     * everything else is the package name.
     *
     * @param fullyQualifiedName class name
     */
    protected PackageClass(String fullyQualifiedName) {
        String processedName = removeDotJava(fullyQualifiedName);
        int dotIndex = processedName.lastIndexOf('.');
        if (dotIndex == -1) {
            this.pkg = null;
            this.fileName = removeDotJava(processedName);
        } else {
            this.pkg = processedName.substring(0, dotIndex);
            this.fileName = removeDotJava(processedName.substring(dotIndex + 1));
        }
    }

    /**
     * Constructor taking the package and file names.  This constructor defaults the output to not end with .java.
     *
     * @param pkg      package name
     * @param fileName file name
     */
    public PackageClass(String pkg, String fileName) {
        this.pkg = pkg;
        this.fileName = removeDotJava(fileName);
    }

    /**
     * Constructor that parses the class package and name from the input class
     *
     * @param inputClass input
     */
    public PackageClass(Class<?> inputClass) {
        if (inputClass.getPackage() != null) {
            this.fileName = inputClass.getName().substring(inputClass.getPackage().getName().length() + 1).replace('$', '.');
            this.pkg = inputClass.getPackage().getName();
        } else {
            this.fileName = inputClass.getName().replace('$', '.');
            this.pkg = null;
        }
    }

    private String removeDotJava(String input) {
        if (input.endsWith(DOT_JAVA)) {
            return input.substring(0, input.length() - DOT_JAVA.length());
        }
        return input;
    }

    /**
     * Builds and returns the name of the class represented by this builder
     *
     * @return class name
     */
    public String getClassName() {
        return removeDotJava(fileName).replace('.', '$');
    }

    /**
     * Builds and returns the fully qualified class name represented by this class
     *
     * @return fully qualified class name
     */
    public String getFullyQualifiedName() {
        if (StringUtils.isEmpty(pkg)) {
            return getClassName();
        }
        return pkg + "." + getClassName();
    }

    public String getPackage() {
        return pkg;
    }

    /**
     * Appends to the class name the given input
     *
     * @param addName input to be appended
     * @return this for method chaining
     */
    public PackageClass append(String addName) {
        return new PackageClass(pkg, fileName + addName);
    }

    /**
     * Replaces the name of the current class with the given input.
     *
     * @param replacement input
     * @return this for method chaining
     */
    public PackageClass replaceName(String replacement) {
        return new PackageClass(pkg, replacement);
    }

    public String toString() {
        return getCanonicalName();
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

    public String getCanonicalName() {
        if (pkg == null) {
            return fileName;
        }
        return pkg + "." + fileName;
    }
}
