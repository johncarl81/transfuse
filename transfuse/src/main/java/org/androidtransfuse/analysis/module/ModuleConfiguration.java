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
package org.androidtransfuse.analysis.module;

/**
 * Acts as a lazy configuration to allow all the type processing / analysis to occur.  This is required to allow for
 * errors during processing and analysis.  When errors are found the given configuration run is rolled back and retried.
 * If a configuration was previously set up this would result in duplicates.  By following this strategy, duplicates
 * are eliminated.
 *
 * @author John Ericksen
 */
public interface ModuleConfiguration {

    /**
     * Set a configuration during the second phase of configuration processing.
     *
     * Should never throw a TransactionRuntimeException.
     */
    void setConfiguration();
}
