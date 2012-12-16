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
package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class AOPRepository {

    private final Map<ASTType, ASTType> interceptorAnnotationMap = new HashMap<ASTType, ASTType>();

    public void put(ASTType annotationType, ASTType interceptor) {
        interceptorAnnotationMap.put(annotationType, interceptor);
    }

    public ASTType getInterceptor(ASTType annotationType) {
        return interceptorAnnotationMap.get(annotationType);
    }

    public boolean isInterceptor(ASTAnnotation annotation) {
        return interceptorAnnotationMap.containsKey(annotation.getASTType());
    }
}
