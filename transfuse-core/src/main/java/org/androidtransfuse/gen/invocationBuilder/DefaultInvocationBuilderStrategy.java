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
package org.androidtransfuse.gen.invocationBuilder;

import org.androidtransfuse.adapter.ASTAccessModifier;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class DefaultInvocationBuilderStrategy implements InvocationBuilderStrategy{

    private final Provider<PublicInjectionBuilder> publicProvider;
    private final Provider<ProtectedInjectionBuilder> protectedProvider;
    private final Provider<PrivateInjectionBuilder> privateProvider;

    @Inject
    public DefaultInvocationBuilderStrategy(Provider<PublicInjectionBuilder> publicProvider,
                                            Provider<ProtectedInjectionBuilder> protectedProvider,
                                            Provider<PrivateInjectionBuilder> privateProvider) {
        this.publicProvider = publicProvider;
        this.protectedProvider = protectedProvider;
        this.privateProvider = privateProvider;
    }

    @Override
    public ModifierInjectionBuilder getInjectionBuilder(ASTAccessModifier modifier) {
        switch (modifier) {
            case PUBLIC:
                return publicProvider.get();
            case PACKAGE_PRIVATE:
            case PROTECTED:
                return protectedProvider.get();
            default:
                return privateProvider.get();
        }
    }
}
