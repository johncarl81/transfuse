package org.androidrobotics.analysis.adapter;

import org.androidrobotics.util.CollectionConverter;

import javax.inject.Inject;
import javax.lang.model.element.Element;

/**
 * @author John Ericksen
 */
public class ASTElementConverterFactory {

    @Inject
    private ASTElementFactory astElementFactory;

    public <T> CollectionConverter<? super Element, T> buildASTElementConverter(Class<T> astTypeClass) {
        return new ASTElementConverter<T>(astTypeClass, astElementFactory);
    }
}
