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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Configures a factory method to provide an instance of the return type for injection in a {@code @TransfuseManifest}
 * annotated configuration and binds it to the return type.</p>
 *
 * <p>
 * For instance, the following module configuration builds a {@code Telescope} object from a {@code Mirror},
 * {@code Tube} and {@code Eyepiece} object:
 * <pre>
 *     {@literal @}TransfuseModule
 *     public void ModuleExample{
 *         {@literal @}Provides
 *         public Telescope buildTelescope(Mirror mirror, Tube tube, Eyepiece eyepiece){
 *             return new SchmidtCassegrainTelescope(mirror, tube, eyepiece);
 *         }
 *     }
 * </pre>
 * </p>
 *
 * <p>
 * Qualifier annotations as well as scoping annotations may be used in addition to the {@code @Provides} annotation
 * for the desired effect.
 * </p>
 *
 * @author John Ericksen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Provides {}