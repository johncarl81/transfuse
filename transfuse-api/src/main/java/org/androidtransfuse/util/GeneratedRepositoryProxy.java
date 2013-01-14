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
package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public class GeneratedRepositoryProxy<T> {

    private final String repositoryPackage;
    private final String repositoryName;
    private T instance;

    public GeneratedRepositoryProxy(String repositoryPackage, String repositoryName) {
        this.repositoryPackage = repositoryPackage;
        this.repositoryName = repositoryName;
        update(getClass().getClassLoader());
    }

    public T get() {
        return instance;
    }

    public final void update(ClassLoader classLoader){
        try{
            Class injectorClass = classLoader.loadClass(repositoryPackage + "." + repositoryName);
            instance = (T) injectorClass.newInstance();
        } catch (ClassNotFoundException e) {
            instance = null;
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to instantiate generated Repository", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to access generated Repository", e);
        }
    }
}
