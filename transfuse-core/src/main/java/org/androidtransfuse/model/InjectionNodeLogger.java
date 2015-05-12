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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class InjectionNodeLogger {

    private final StringBuilder builder;
    private final List<InjectionNode> nodes = new ArrayList<InjectionNode>();
    private final Set<InjectionNode> visited = new HashSet<InjectionNode>();
    private int indent = 0;

    public InjectionNodeLogger(StringBuilder builder, InjectionNode node) {
        nodes.add(node);
        this.builder = builder;
    }

    public InjectionNodeLogger append(Object input) {
        if(indent > 0){
            for(int i = 0; i < indent ; i++){
                input = input.toString().replace("\n", "\n\t");
            }
        }
        builder.append(input);
        return this;
    }

    public boolean containsUnvisitedNodes(){
        for (InjectionNode node : nodes) {
            if(!visited.contains(node)){
                return true;
            }
        }
        return false;
    }

    public void pushNode(InjectionNode node){
        nodes.add(node);
    }

    public InjectionNode next(){
        InjectionNode next;
        for(InjectionNode node : nodes){
            if(!visited.contains(node)){
                visited.add(node);
                return node;
            }
        }
        return null;
    }

    public void pushIndent() {
        indent++;
    }

    public void popIndent() {
        indent--;
    }
}
