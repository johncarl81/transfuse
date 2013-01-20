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

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.annotation.*;

/**
 * <p>
 * On a {@code @TransfuseModule} class, specifying a {@code @BindInterceptor} associates an interceptor annotation to
 * the {@code MethodInterceptor} implementation.</p>
 * <p>
 * Example:
 * <pre>
 *     {@code @TransfuseModule}
 *     {@code @BindInterceptor(Asynchronous.class, AsynchronousMethodInterceptor.class)}
 *     public class Module{}
 * </pre>
 * </p>
 * <p>
 * Every time a method is annotated with {@code @Asynchronous}, Transfuse will wrap the method ith the
 * {@code AsynchronousMethodInterceptor} Method Interceptor.
 * </p>
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindInterceptor {

    Class<? extends Annotation> annotation();

    Class<? extends MethodInterceptor> interceptor();
}
