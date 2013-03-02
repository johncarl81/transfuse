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

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * <p>Injection qualifier annotation specifying the injection should be drawn from the Bundle Extras by extra name.  Extras
 * represent the contract of the construction of an Activity or Service and Transfuse uses these annotated fields
 * to build the IntentFactoryStrategy classes.  Extras can either be required or optional.  Required Extras will be
 * validated to be not-null whereas not-required Extras may be null.  Additionally, Required Extras will provided via
 * parameters in the constructor of the Activity or Service's IntentFactoryStrategy while non-required Extras will be
 * provided via optional setter methods.</p>
 *
 * @author John Ericksen
 */
@Target({METHOD, CONSTRUCTOR, FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Extra {

    /**
     * Name identifier of the Extra.  Must follow java variable name syntax.
     */
    String value();

    /**
     * Optional value.  Specifies the validation and IntentFactoryStrategy mutation techniques.
     */
    boolean optional() default false;
}