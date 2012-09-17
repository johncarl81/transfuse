package org.androidtransfuse.analysis.adapter;

import com.google.common.base.Function;

import javax.inject.Inject;
import javax.lang.model.element.Element;

/**
 * Factory to build the ASTElementConverter
 *
 * @author John Ericksen
 */
public class ASTElementConverterFactory {

    @Inject
    private ElementConverterFactory elementConverterFactory;

    public <T> Function<? super Element, T> buildASTElementConverter(Class<T> astTypeClass) {
        return new ASTElementConverter<T>(astTypeClass, elementConverterFactory);
    }
}
