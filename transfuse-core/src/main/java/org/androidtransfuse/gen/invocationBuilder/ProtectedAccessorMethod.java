/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.JInvocation;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.gen.ClassGenerationUtil;

/**
 * @author John Ericksen
 */
public class ProtectedAccessorMethod {

    private final PackageClass helperClass;
    private final String method;

    public ProtectedAccessorMethod(PackageClass helperClass, String method) {
        this.helperClass = helperClass;
        this.method = method;
    }

    public JInvocation invoke(ClassGenerationUtil generationUtil) {
        return generationUtil.ref(helperClass).staticInvoke(method);
    }
}
