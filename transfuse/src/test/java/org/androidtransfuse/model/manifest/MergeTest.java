/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
