/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.integrationTest;

import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.aop.AOPInterceptor;
import org.androidtransfuse.integrationTest.aop.DependencyInterceptor;
import org.androidtransfuse.integrationTest.aop.InjectedInterceptor;
import org.androidtransfuse.integrationTest.aop.InterceptorRecorder;
import org.androidtransfuse.integrationTest.externalGenerator.Proxied;
import org.androidtransfuse.integrationTest.externalGenerator.ProxiedProxy;
import org.androidtransfuse.integrationTest.inject.*;
import org.androidtransfuse.integrationTest.layout.RandomProvider;

import java.util.Random;

/**
 * @author John Ericksen
 */
@TransfuseModule
@BindInterceptors({
        @BindInterceptor(annotation = AOPInterceptor.class, interceptor = InterceptorRecorder.class),
        @BindInterceptor(annotation = DependencyInterceptor.class, interceptor = InjectedInterceptor.class)
})
@BindProviders({
        @BindProvider(type = ProvidedInjectTarget.class, provider = InjectTargetProvider.class),
        @BindProvider(type = Random.class, provider = RandomProvider.class)
})
@Bindings({
        @Bind(type = LoopThree.class, to = LoopThreeImpl.class),
        @Bind(type = Proxied.class, to = ProxiedProxy.class)
})
public class IntegrationModule {

    @Provides
    public GenericType<Concrete> buildTarget2(ConcreteType concreteType){
        return concreteType;
    }

    @Provides
    public GenericType<Concrete2> buildTarget(){
        return new Concrete2Type();
    }
}
