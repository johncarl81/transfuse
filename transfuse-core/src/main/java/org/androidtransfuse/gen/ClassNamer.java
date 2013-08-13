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
package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.Namer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author John Ericksen
 */
@Singleton
public class ClassNamer {

    private static final Map<String, String> PROHIBITED_PACKAGES = new HashMap<String, String>();

    static{
        PROHIBITED_PACKAGES.put("java.", "java_.");
        PROHIBITED_PACKAGES. put("javax.", "javax_.");
    }

    private final ConcurrentMap<String, AtomicInteger> nameMap = new ConcurrentHashMap<String, AtomicInteger>();
    private final String namespace;

    @Inject
    public ClassNamer(){
        this(null);
    }

    public ClassNamer(String namespace) {
        this.namespace = namespace;
    }

    public static final class ClassNameBuilder{
        private final PackageClass packageClass;
        private final ClassNamer classNamer;
        private final String namespace;
        private boolean namespaced;
        private String appendment;

        public ClassNameBuilder(PackageClass packageClass, String namespace, ClassNamer classNamer) {
            this.packageClass = packageClass;
            this.namespace = namespace;
            this.classNamer = classNamer;
        }

        public ClassNameBuilder namespaced(){
            this.namespaced = true;
            return this;
        }

        public ClassNameBuilder append(String appendment) {
            this.appendment = appendment;
            return this;
        }

        public PackageClass build(){
            return new PackageClass(
                    getPackage(packageClass.getPackage()),
                    generateName(namespaced, packageClass.getClassName(), appendment, classNamer));
        }

        private String getPackage(String inputPackage){
            if(inputPackage == null){
                return null;
            }
            // Avoid prohibited package names
            String validPackage = inputPackage;
            for (Map.Entry<String, String> prohibitedPackage : PROHIBITED_PACKAGES.entrySet()) {
                if(inputPackage.startsWith(prohibitedPackage.getKey())){
                    validPackage = inputPackage.replaceFirst(prohibitedPackage.getKey(), prohibitedPackage.getValue());
                }
            }
            return validPackage;
        }

        private String generateName(boolean namespaced, String fullClassName, String appendment, ClassNamer classNamer) {

            //remove array notation
            String sanitizedFullClassName = fullClassName.replaceAll("\\[\\]", "");

            String className = sanitizedFullClassName;
            if (fullClassName.contains(".")) {
                className = sanitizedFullClassName.substring(sanitizedFullClassName.lastIndexOf('.') + 1);
            }

            // build class name with the following format:
            // <lower case> + classname + _ + #
            Namer namer;

            if(namespaced && namespace != null){
                namer = Namer.name(namespace).append(className);
            }
            else{
                namer = Namer.name(className);
            }

            if(appendment != null){
                namer.append(appendment);
            }

            String nameRoot = namer.build();

            if(classNamer != null){
                namer.append(classNamer.get(nameRoot));
            }
            return namer.build();
        }
    }

    public static ClassNameBuilder className(PackageClass packageClass) {
        return className(packageClass, null, null);
    }

    public static ClassNameBuilder className(InjectionNode injectionNode) {
        return className(injectionNode.getASTType());
    }

    public static ClassNameBuilder className(ASTType astType) {
        return className(astType.getPackageClass());
    }

    public static ClassNameBuilder className(JDefinedClass definedClass) {
        return className(new PackageClass(definedClass._package().name(), definedClass.name()));
    }

    private static ClassNameBuilder className(PackageClass packageClass, String namespace, ClassNamer classNamer){
        return new ClassNameBuilder(packageClass, namespace, classNamer);
    }

    public ClassNameBuilder numberedClassName(PackageClass packageClass){
        return className(packageClass, namespace, this);
    }

    public ClassNameBuilder numberedClassName(Class clazz) {
        return numberedClassName(new PackageClass(clazz));
    }

    public ClassNameBuilder numberedClassName(ASTType astType) {
        return numberedClassName(astType.getPackageClass());
    }

    public ClassNameBuilder numberedClassName(JDefinedClass definedClass) {
        return numberedClassName(new PackageClass(definedClass._package().name(), definedClass.name()));
    }

    private int get(String name){
        AtomicInteger result = nameMap.get(name);
        if (result == null) {
            AtomicInteger value = new AtomicInteger();
            result = nameMap.putIfAbsent(name, value);
            if (result == null) {
                result = value;
            }
        }

        return result.getAndIncrement();
    }
}
