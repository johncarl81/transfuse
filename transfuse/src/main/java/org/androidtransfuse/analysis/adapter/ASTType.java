package org.androidtransfuse.analysis.adapter;

import java.util.Collection;
import java.util.List;

/**
 * Abstract Syntax Tree Type node
 *
 * @author John Ericksen
 */
public interface ASTType extends ASTBase {

    /**
     * Supplies all available methods
     *
     * @return available methods
     */
    Collection<ASTMethod> getMethods();

    /**
     * Supplies all available fields
     *
     * @return fields
     */
    Collection<ASTField> getFields();

    /**
     * Supplies all available constructors
     *
     * @return constructors
     */
    Collection<ASTConstructor> getConstructors();

    /**
     * Determines if the given AST type represents a concrete class
     *
     * @return concrete class status
     */
    boolean isConcreteClass();

    /**
     * Supplies the super class (by extension) of this type
     *
     * @return supertype
     */
    ASTType getSuperClass();

    /**
     * Supplies the list of implemented interfaces
     *
     * @return interfaces implemented
     */
    Collection<ASTType> getInterfaces();

    /**
     * Determines if this type is an array type
     *
     * @return array type
     */
    boolean isArray();

    /**
     * returns a list of the generic type parameters, if they are appropriate for the type and exist.
     *
     * @return generic parameters
     */
    List<ASTType> getGenericParameters();

    boolean inheritsFrom(ASTType type);

    boolean extendsFrom(ASTType type);

    boolean implementsFrom(ASTType type);
}
