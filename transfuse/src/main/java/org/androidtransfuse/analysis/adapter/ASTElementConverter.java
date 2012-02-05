package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.util.CollectionConverter;

import javax.lang.model.element.Element;

/**
 * Element to AST converter, converts the given type of javax.lang.model.element.Element to the
 * transfuse internal AST representation.
 *
 * @author John Ericksen
 */
public class ASTElementConverter<T> implements CollectionConverter<Element, T> {

    private Class<T> astTypeClass;
    private ElementConverterFactory elementConverterFactory;

    public ASTElementConverter(Class<T> astTypeClass, ElementConverterFactory elementConverterFactory) {
        this.astTypeClass = astTypeClass;
        this.elementConverterFactory = elementConverterFactory;
    }

    @Override
    public T convert(Element element) {
        //visit the given element to determine its type, feed it into the appropriate
        //ASTElementFactory method and return the result
        return element.accept(elementConverterFactory.buildTypeConverter(astTypeClass), null);
    }
}
