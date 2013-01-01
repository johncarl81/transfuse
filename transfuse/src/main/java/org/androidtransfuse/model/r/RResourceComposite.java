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
package org.androidtransfuse.model.r;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class RResourceComposite implements RResource {

    private final ImmutableList<RResource> resources;

    public RResourceComposite(RResource... resources) {
        this.resources = FluentIterable
                .from(Arrays.asList(resources))
                .filter(Predicates.notNull())
                .toImmutableList();
    }

    @Override
    public ResourceIdentifier getResourceIdentifier(Integer id) {

        for (RResource resource : resources) {
            ResourceIdentifier identifier = resource.getResourceIdentifier(id);
            if (identifier != null) {
                return identifier;
            }
        }
        return null;
    }
}
