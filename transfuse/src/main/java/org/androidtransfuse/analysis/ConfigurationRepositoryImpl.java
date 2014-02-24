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
package org.androidtransfuse.analysis;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.androidtransfuse.ComponentBuilder;
import org.androidtransfuse.ConfigurationRepository;
import org.androidtransfuse.EventMapping;
import org.androidtransfuse.InjectionMapping;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.util.matcher.Matcher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author John Ericksen
 */
@Singleton
public class ConfigurationRepositoryImpl implements ConfigurationRepository{

    private final ASTClassFactory astClassFactory;

    private final Map<Matcher<AnnotatedType>, Configuration> configurations = new HashMap<Matcher<AnnotatedType>, Configuration>();

    private static class Configuration{
        private final List<EventMapping> events = new ArrayList<EventMapping>();
        private final List<InjectionMapping> mappings = new ArrayList<InjectionMapping>();
        private final List<SuperCallMapping> superCalls = new ArrayList<SuperCallMapping>();
        private final Set<Class<?>> callThroughEvents = new HashSet<Class<?>>();
        private final Map<ASTType, String> listeners = new HashMap<ASTType, String>();
        private Registration registraion = null;
    }

    @Inject
    public ConfigurationRepositoryImpl(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    private List<Configuration> findConfigurations(ASTType type, Class<? extends Annotation> annotation){
        AnnotatedType signature = new AnnotatedType(type, astClassFactory.getType(annotation));
        List<Configuration> matched = new ArrayList<Configuration>();
        for (Map.Entry<Matcher<AnnotatedType>,Configuration> configurationEntry : configurations.entrySet()) {
            if(configurationEntry.getKey().matches(signature)){
                matched.add(configurationEntry.getValue());
            }
        }
        return matched;
    }

    private Configuration getConfiguration(ASTType type, Class<? extends Annotation> annotation){
        AnnotatedTypeMatcher annotatedTypeMatcher = new AnnotatedTypeMatcher(type, astClassFactory.getType(annotation));
        if(!configurations.containsKey(annotatedTypeMatcher)){
            configurations.put(annotatedTypeMatcher, new Configuration());
        }
        return configurations.get(annotatedTypeMatcher);
    }

    @Override
    public void addRegistration(Class<? extends Annotation> componentAnnotation, ASTType componentType, String methodName, List<ASTType> parameters) {
        getConfiguration(componentType, componentAnnotation).registraion = new Registration(methodName, parameters);
    }

    @Override
    public void addSuperCall(Class<? extends Annotation> componentAnnotation, ASTType componentType, String methodName, List<ASTType> parameters) {
        getConfiguration(componentType, componentAnnotation).superCalls.add(new SuperCallMapping(methodName, parameters));
    }

    @Override
    public ComponentBuilder component(Class<? extends Annotation> annotation) {
        return new ComponentBuilder(this, annotation);
    }

    @Override
    public void addEvent(Class<? extends Annotation> componentType, ASTType type, EventMapping eventMapping) {
        getConfiguration(type, componentType).events.add(eventMapping);
    }

    public void addMapping(Class<? extends Annotation> componentType, ASTType type, InjectionMapping injectionMapping) {
        getConfiguration(type, componentType).mappings.add(injectionMapping);
    }

    @Override
    public void addCallThroughEvent(Class<? extends Annotation> componentType, ASTType type, Class<?> callThroughEventClass) {
        getConfiguration(type, componentType).callThroughEvents.add(callThroughEventClass);
    }

    @Override
    public void addListener(Class<? extends Annotation> componentType, ASTType type, ASTType listenerType, String listenerMethod) {
        getConfiguration(type, componentType).listeners.put(listenerType, listenerMethod);
    }

    public List<EventMapping> getEvents(ASTType type, Class<? extends Annotation> annotation){
        return FluentIterable.from(findConfigurations(type, annotation))
                .transformAndConcat(new Function<Configuration, Iterable<EventMapping>>() {
                    public Iterable<EventMapping> apply(Configuration configuration) {
                        return configuration.events;
                    }
                }).toList();
    }

    public List<InjectionMapping> getMappings(ASTType type, Class<? extends Annotation> annotation) {
        return FluentIterable.from(findConfigurations(type, annotation))
                .transformAndConcat(new Function<Configuration, Iterable<InjectionMapping>>() {
                    public Iterable<InjectionMapping> apply(Configuration configuration) {
                        return configuration.mappings;
                    }
                }).toList();
    }

    public Map<ASTType, String> getListeners(ASTType type, Class<? extends Annotation> annotation) {
        Map<ASTType, String> matchedListeners = new HashMap<ASTType, String>();

        for (Configuration configuration : findConfigurations(type, annotation)) {
            matchedListeners.putAll(configuration.listeners);
        }

        return matchedListeners;
    }

    public Set<Class<?>> getCallThroughClasses(ASTType type, Class<? extends Annotation> annotation) {
        return FluentIterable.from(findConfigurations(type, annotation))
                .transformAndConcat(new Function<Configuration, Iterable<Class<?>>>() {
                    public Iterable<Class<?>> apply(Configuration configuration) {
                        return configuration.callThroughEvents;
                    }
                }).toSet();
    }

    public List<SuperCallMapping> getSuperCalls(ASTType type, Class<? extends Annotation> annotation){
        return FluentIterable.from(findConfigurations(type, annotation))
                .transformAndConcat(new Function<Configuration, Iterable<SuperCallMapping>>() {
                    public Iterable<SuperCallMapping> apply(Configuration configuration) {
                        return configuration.superCalls;
                    }
                }).toList();
    }

    public Registration getRegistration(ASTType type, Class<? extends Annotation> annotation){
        for(Configuration configuration : findConfigurations(type, annotation)){
            if(configuration.registraion != null){
                return configuration.registraion;
            }
        }
        return null;
    }
}
