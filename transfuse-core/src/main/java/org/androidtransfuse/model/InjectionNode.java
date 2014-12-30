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
package org.androidtransfuse.model;

import org.androidtransfuse.adapter.ASTType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNode {

    private final InjectionSignature signature;
    private final InjectionSignature astType;
    private final Map<Class, Object> aspects = new HashMap<Class, Object>();

    public InjectionNode(InjectionSignature signature) {
        this(signature, signature);
    }

    public InjectionNode(InjectionSignature signature, InjectionSignature astType) {
        this.astType = astType;
        this.signature = signature;
    }

    public InjectionSignature getSignature(){
        return signature;
    }

    public InjectionSignature getTypeSignature(){
        //todo: rename to something else.
        return astType;
    }

    public ASTType getUsageType() {
        return signature.getType();
    }

    public String getClassName() {
        return getASTType().getName();
    }

    public ASTType getASTType() {
        return astType.getType();
    }

    public <T> T getAspect(Class<T> clazz){
        return (T) aspects.get(clazz);
    }

    public void addAspect(Object object) {
        aspects.put(object.getClass(), object);
    }

    public <T> void addAspect(Class<T> clazz, T object) {
        aspects.put(clazz, object);
    }

    public boolean containsAspect(Class<?> clazz) {
        return aspects.containsKey(clazz);
    }

    public Map<Class, Object> getAspects() {
        return aspects;
    }
}
