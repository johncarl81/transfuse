/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.annotations;

import org.androidtransfuse.scope.EmptyScope;

import javax.inject.Named;
import javax.inject.Provider;
import java.lang.annotation.*;

/**
 * <p>
 * On a {@code @TransfuseModule} class, defining a {@code Provider} binding with {@code @BindProvider} defines a
 * relationship between a class and a {@code Provider}.  Each time the given type is requested to be injected, the
 * defined provider's {@code Provider.get()} method will be used to request an instance.</p>
 * <p>
 * Example:
 * <pre>
 *     {@code @TransfuseModule}
 *     {@code @BindProvider(type=Cat.class, to=OrangeTabbyProvider.class)}
 *     public class Module{}
 * </pre>
 * </p>
 * <p>
 * In each instance where a {@code Cat} is injected, the return value from the get() method on an instance of the
 * {@code OrangeTabbyProvider} class will be used.
 * </p>
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindProvider {

    Class<?> type();

    Class<? extends Provider> provider();

    Class<? extends Annotation> scope() default EmptyScope.class;

    Named named() default @Named;
}
