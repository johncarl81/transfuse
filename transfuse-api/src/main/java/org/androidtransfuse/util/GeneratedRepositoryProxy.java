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
 * Centralized repository loader which, upon construction, instantiates an instance of the given Repository using
 * reflection.  Although reflection is frowned upon when seeking performance, this class is typically used in a
 * static context, so it is only loaded once per application and thus the reflection calls are kept to a minimum.
 *
 * @author John Ericksen
 */
public class GeneratedRepositoryProxy<T> {

    private final String repositoryPackage;
    private final String repositoryName;
    private T instance = null;

    public GeneratedRepositoryProxy(String repositoryPackage, String repositoryName) {
        this.repositoryPackage = repositoryPackage;
        this.repositoryName = repositoryName;
        update(getClass().getClassLoader());
    }

    /**
     * returns the contained Repository instance.  If no instance was able to be constructed then this method will
     * throw a TransfuseRuntimeException if called.
     *
     * @throws TransfuseRuntimeException
     * @return Repository instance
     */
    public T get() {
        if(instance == null){
            throw new TransfuseRuntimeException("Unable to find " + repositoryName + " class");
        }
        return instance;
    }

    /**
     * Update the repository class from the given classloader.  If the given repository class cannot be instantiated
     * then this method will throw a TransfuseRuntimeException.
     *
     * @throws TransfuseRuntimeException
     * @param classLoader
     */
    public final void update(ClassLoader classLoader){
        try{
            Class repositoryClass = classLoader.loadClass(repositoryPackage + "." + repositoryName);
            instance = (T) repositoryClass.newInstance();
        } catch (ClassNotFoundException e) {
            instance = null;
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to instantiate generated Repository", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to access generated Repository", e);
        }
    }
}
