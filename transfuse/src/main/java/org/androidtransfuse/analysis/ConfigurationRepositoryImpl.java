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

import org.androidtransfuse.ComponentBuilder;
import org.androidtransfuse.ConfigurationRepository;
import org.androidtransfuse.EventMapping;
import org.androidtransfuse.InjectionMapping;
import org.androidtransfuse.adapter.ASTStringType;
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
    private final Map<Matcher<AnnotatedType>, List<EventMapping>> events = new HashMap<Matcher<AnnotatedType>, List<EventMapping>>();
    private final Map<Matcher<AnnotatedType>, List<InjectionMapping>> mappings = new HashMap<Matcher<AnnotatedType>, List<InjectionMapping>>();
    private final Map<Matcher<AnnotatedType>, Set<Class<?>>> callThroughEvents = new HashMap<Matcher<AnnotatedType>, Set<Class<?>>>();
    private final Map<Matcher<AnnotatedType>, Map<ASTType, String>> listeners = new HashMap<Matcher<AnnotatedType>, Map<ASTType, String>>();

    @Inject
    public ConfigurationRepositoryImpl(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    public List<EventMapping> getEvents(ASTType type, Class<? extends Annotation> annotation){
        List<EventMapping> matched = new ArrayList<EventMapping>();
        AnnotatedType signature = new AnnotatedType(type, astClassFactory.getType(annotation));

        for (Map.Entry<Matcher<AnnotatedType>, List<EventMapping>> eventEntry : events.entrySet()) {
            if(eventEntry.getKey().matches(signature)){
                matched.addAll(eventEntry.getValue());
            }
        }

        return matched;
    }

    @Override
    public ComponentBuilder component(Class<? extends Annotation> annotation) {
        return new ComponentBuilder(this, annotation);
    }

    @Override
    public void addEvent(Class<? extends Annotation> componentType, String type, EventMapping eventMapping) {
        ASTType matchType = null;
        if(type != null){
            matchType = new ASTStringType(type);
        }
        AnnotatedTypeMatcher annotatedTypeMatcher = new AnnotatedTypeMatcher(matchType, astClassFactory.getType(componentType));

        if(!events.containsKey(annotatedTypeMatcher)){
            events.put(annotatedTypeMatcher, new ArrayList<EventMapping>());
        }
        events.get(annotatedTypeMatcher).add(eventMapping);
    }

    public void addMapping(Class<? extends Annotation> componentType, String type, InjectionMapping eventMapping) {
        ASTType matchType = null;
        if(type != null){
            matchType = new ASTStringType(type);
        }
        AnnotatedTypeMatcher annotatedTypeMatcher = new AnnotatedTypeMatcher(matchType, astClassFactory.getType(componentType));

        if(!mappings.containsKey(annotatedTypeMatcher)){
            mappings.put(annotatedTypeMatcher, new ArrayList<InjectionMapping>());
        }
        mappings.get(annotatedTypeMatcher).add(eventMapping);
    }

    @Override
    public void addCallThroughEvent(Class<? extends Annotation> componentType, String type, Class<?> callThroughEventClass) {
        ASTType matchType = null;
        if(type != null){
            matchType = new ASTStringType(type);
        }
        AnnotatedTypeMatcher annotatedTypeMatcher = new AnnotatedTypeMatcher(matchType, astClassFactory.getType(componentType));

        if(!callThroughEvents.containsKey(annotatedTypeMatcher)){
            callThroughEvents.put(annotatedTypeMatcher, new HashSet<Class<?>>());
        }
        callThroughEvents.get(annotatedTypeMatcher).add(callThroughEventClass);
    }

    @Override
    public void addListener(Class<? extends Annotation> componentType, String type, ASTType listenerType, String listenerMethod) {
        ASTType matchType = null;
        if(type != null){
            matchType = new ASTStringType(type);
        }
        AnnotatedTypeMatcher annotatedTypeMatcher = new AnnotatedTypeMatcher(matchType, astClassFactory.getType(componentType));

        if(!listeners.containsKey(annotatedTypeMatcher)){
            listeners.put(annotatedTypeMatcher, new HashMap<ASTType, String>());
        }
        listeners.get(annotatedTypeMatcher).put(listenerType, listenerMethod);
    }

    public List<InjectionMapping> getMappings(ASTType type, Class<? extends Annotation> annotation) {
        List<InjectionMapping> matched = new ArrayList<InjectionMapping>();
        AnnotatedType signature = new AnnotatedType(type, astClassFactory.getType(annotation));

        for (Map.Entry<Matcher<AnnotatedType>, List<InjectionMapping>> eventEntry : mappings.entrySet()) {
            if(eventEntry.getKey().matches(signature)){
                matched.addAll(eventEntry.getValue());
            }
        }

        return matched;
    }

    public Map<ASTType, String> getListeners(ASTType type, Class<? extends Annotation> annotation) {
        Map<ASTType, String> matchedListeners = new HashMap<ASTType, String>();

        AnnotatedType signature = new AnnotatedType(type, astClassFactory.getType(annotation));

        for (Map.Entry<Matcher<AnnotatedType>, Map<ASTType, String>> eventEntry : listeners.entrySet()) {
            if(eventEntry.getKey().matches(signature)){
                matchedListeners.putAll(eventEntry.getValue());
            }
        }

        return matchedListeners;
    }

    public Set<Class<?>> getCallThroughClasses(ASTType type, Class<? extends Annotation> annotation) {
        Set<Class<?>> matched = new HashSet<Class<?>>();

        AnnotatedType signature = new AnnotatedType(type, astClassFactory.getType(annotation));

        for (Map.Entry<Matcher<AnnotatedType>, Set<Class<?>>> eventEntry : callThroughEvents.entrySet()) {
            if(eventEntry.getKey().matches(signature)){
                matched.addAll(eventEntry.getValue());
            }
        }

        return matched;
    }
}
