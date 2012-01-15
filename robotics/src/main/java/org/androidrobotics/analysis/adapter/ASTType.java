package org.androidrobotics.analysis.adapter;

import java.util.Collection;

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

    ASTType getSuperClass();

    Collection<ASTType> getInterfaces();

    boolean isArray();
}
