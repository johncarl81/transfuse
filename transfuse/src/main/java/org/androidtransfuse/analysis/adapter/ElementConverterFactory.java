package org.androidtransfuse.analysis.adapter;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Factory to create Element Converters
 *
 * @author John Ericksen
 */
public class ElementConverterFactory {

    private ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private Provider<ASTElementFactory> astElementFactoryProvider;

    @Inject
    public ElementConverterFactory(ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                   Provider<ASTElementFactory> astElementFactoryProvider) {
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.astElementFactoryProvider = astElementFactoryProvider;
    }

    public <T> ASTTypeElementConverter<T> buildTypeConverter(Class<T> clazz) {
        return new ASTTypeElementConverter<T>(clazz, astElementFactoryProvider.get());
    }

    public <T> AnnotationTypeValueConverterVisitor<T> buildAnnotationValueConverter(Class<T> clazz) {
        return new AnnotationTypeValueConverterVisitor<T>(clazz, astTypeBuilderVisitor, this);
    }
}
