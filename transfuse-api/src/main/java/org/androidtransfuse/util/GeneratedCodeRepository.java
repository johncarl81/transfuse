package org.androidtransfuse.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author John Ericksen
 */
public abstract class GeneratedCodeRepository<T> {

    private ConcurrentMap<Class, T> generatedMap = new ConcurrentHashMap<Class, T>();

    public GeneratedCodeRepository(String repositoryPackage, String repositoryName) {
        loadRepository(getClass().getClassLoader(), repositoryPackage, repositoryName);
    }

    public T get(Class clazz){
        T result = generatedMap.get(clazz);
        if (result == null) {
            T value = findClass(clazz);
            if(value == null){
                return null;
            }
            result = generatedMap.putIfAbsent(clazz, value);
            if (result == null) {
                result = value;
            }
        }

        return result;
    }

    public abstract T findClass(Class clazz);

    /**
     * Update the repository class from the given classloader.  If the given repository class cannot be instantiated
     * then this method will throw a TransfuseRuntimeException.
     *
     * @throws TransfuseRuntimeException
     * @param classLoader
     */
    public final void loadRepository(ClassLoader classLoader, String repositoryPackage, String repositoryName){
        try{
            Class repositoryClass = classLoader.loadClass(repositoryPackage + "." + repositoryName);
            Repository<T> instance = (Repository<T>) repositoryClass.newInstance();
            generatedMap.putAll(instance.get());

        } catch (ClassNotFoundException e) {
            //nothing
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to instantiate generated Repository", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to access generated Repository", e);
        }
    }
}
