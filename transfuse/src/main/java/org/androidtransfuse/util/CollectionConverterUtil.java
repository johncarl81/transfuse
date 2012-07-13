package org.androidtransfuse.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Converts a collection from one type to another using the supplied conversion class.
 *
 * @author John Ericksen
 */
public class CollectionConverterUtil {

    /**
     * Transform the input collection into the output collection, using the supplied conversion to convert element to element.
     * @param input collection
     * @param conversion instance to convert between 
     * @param <T> intput collection generic type
     * @param <V> output collection generic type
     * @return converted collection
     */
    public <T, V> Collection<V> transform(Collection<? extends T> input, Conversion<? super T, ? extends V> conversion) {
        List<V> wrappedCollection = new ArrayList<V>();
        for (T t : input) {
            V converted = conversion.convert(t);
            if (converted != null) {
                wrappedCollection.add(converted);
            }
        }
        return wrappedCollection;
    }
}
