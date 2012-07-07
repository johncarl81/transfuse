package org.androidtransfuse.model.manifest;

import org.androidtransfuse.processor.Merge;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;

/**
 * @author John Ericksen
 */
public class MergeTest {

    @Test
    public void testApplicationMergeTags(){
        validateUniqueTags(Application.class);
    }

    @Test
    public void testActivityMergeTags(){
        validateUniqueTags(Activity.class);
    }

    @Test
    public void testCategoryMergeTags(){
        validateUniqueTags(Category.class);
    }

    @Test
    public void testIntentFilterMergeTags(){
        validateUniqueTags(IntentFilter.class);
    }

    @Test
    public void testMetaDataMergeTags(){
        validateUniqueTags(MetaData.class);
    }

    @Test
    public void testReceiverMergeTags(){
        validateUniqueTags(Receiver.class);
    }

    @Test
    public void testServiceMergeTags(){
        validateUniqueTags(Service.class);
    }

    @Test
    public void testActionMergeTags(){
        validateUniqueTags(Action.class);
    }

    private void validateUniqueTags(Class<?> clazz) {
        Set<String> tags = new HashSet<String>();
        for (Method method : clazz.getMethods()) {
            if(method.isAnnotationPresent(Merge.class)){
                String tag = method.getAnnotation(Merge.class).value();
                assertFalse(clazz.getName() + " tag: " + tag, tags.contains(tag));
                tags.add(tag);
            }
        }
    }
}
