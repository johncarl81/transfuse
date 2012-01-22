package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ElementConverterFactory {

    @Inject
    ASTTypeBuilderVisitor astTypeBuilderVisitor;
    @Inject
    ElementConverterFactory astTypeElementConverterFactory;
    @Inject
    ASTElementFactory astElementFactory;

    public <T> ASTTypeElementConverter<T> buildTypeConverter(Class<T> clazz) {
        return new ASTTypeElementConverter<T>(clazz, astElementFactory);
    }

    public <T> AnnotationTypeValueConverterVisitor<T> buildAnnotationValueConverter(Class<T> clazz) {
        return new AnnotationTypeValueConverterVisitor<T>(clazz, astTypeBuilderVisitor, astTypeElementConverterFactory);
    }
}
