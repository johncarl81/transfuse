package org.androidtransfuse.analysis.adapter;

import javax.inject.Inject;

/**
 * Factory to create Element Converters
 *
 * @author John Ericksen
 */
public class ElementConverterFactory {

    private ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private ElementConverterFactory astTypeElementConverterFactory;
    private ASTElementFactory astElementFactory;

    public <T> ASTTypeElementConverter<T> buildTypeConverter(Class<T> clazz) {
        return new ASTTypeElementConverter<T>(clazz, astElementFactory);
    }

    public <T> AnnotationTypeValueConverterVisitor<T> buildAnnotationValueConverter(Class<T> clazz) {
        return new AnnotationTypeValueConverterVisitor<T>(clazz, astTypeBuilderVisitor, astTypeElementConverterFactory);
    }

    @Inject
    public void setAstTypeBuilderVisitor(ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    @Inject
    public void setAstTypeElementConverterFactory(ElementConverterFactory astTypeElementConverterFactory) {
        this.astTypeElementConverterFactory = astTypeElementConverterFactory;
    }

    @Inject
    public void setAstElementFactory(ASTElementFactory astElementFactory) {
        this.astElementFactory = astElementFactory;
    }
}
