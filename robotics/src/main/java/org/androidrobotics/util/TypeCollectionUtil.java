package org.androidrobotics.util;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
@Singleton
public class TypeCollectionUtil {

    public <T, V> List<V> wrapCollection(Collection<? extends T> input, CollectionConverter<? super T, ? extends V> converter) {
        List<V> wrappedCollection = new ArrayList<V>();
        for (T t : input) {
            V converted = converter.convert(t);
            if (converted != null) {
                wrappedCollection.add(converted);
            }
        }
        return wrappedCollection;
    }
}
