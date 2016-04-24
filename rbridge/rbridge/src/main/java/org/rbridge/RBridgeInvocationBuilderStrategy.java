/**
 * Copyright 2014-2015 John Ericksen
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
package org.rbridge;

import org.androidtransfuse.adapter.ASTAccessModifier;
import org.androidtransfuse.gen.invocationBuilder.InvocationBuilderStrategy;
import org.androidtransfuse.gen.invocationBuilder.ModifiedInvocationBuilder;
import org.androidtransfuse.gen.invocationBuilder.PublicInvocationBuilder;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class RBridgeInvocationBuilderStrategy implements InvocationBuilderStrategy {

    private final PublicInvocationBuilder publicInvocationBuilder;

    @Inject
    public RBridgeInvocationBuilderStrategy(PublicInvocationBuilder publicInvocationBuilder) {
        this.publicInvocationBuilder = publicInvocationBuilder;
    }

    @Override
    public ModifiedInvocationBuilder getInjectionBuilder(ASTAccessModifier modifier) {
        return publicInvocationBuilder;
    }
}
