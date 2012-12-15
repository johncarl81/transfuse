package org.androidtransfuse.analysis.adapter;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Factory to create Element Converters
 *
 * @author John Ericksen
 */
public class ElementConverterFactory {

    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final Provider<ASTElementFactory> astElementFactoryProvider;
    private final ASTFactory astFactory;

    @Inject
    public ElementConverterFactory(ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                   Provider<ASTElementFactory> astElementFactoryProvider,
                                   ASTFactory astFactory) {
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.astElementFactoryProvider = astElementFactoryProvider;
        this.astFactory = astFactory;
    }

    public <T> ASTTypeElementConverter<T> buildTypeConverter(Class<T> clazz) {
        return new ASTTypeElementConverter<T>(clazz, astElementFactoryProvider.get());
    }

    public <T> AnnotationTypeValueConverterVisitor<T> buildAnnotationValueConverter(Class<T> clazz) {
        return new AnnotationTypeValueConverterVisitor<T>(clazz, astTypeBuilderVisitor, this, astFactory);
    }
}
