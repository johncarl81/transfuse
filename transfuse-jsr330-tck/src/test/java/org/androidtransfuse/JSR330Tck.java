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
package org.androidtransfuse;

import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.Bindings;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.annotations.Provides;
import org.androidtransfuse.bootstrap.BootstrapModule;
import org.androidtransfuse.scope.ConcurrentDoubleLockingScope;
import org.androidtransfuse.scope.Scopes;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.*;
import org.atinject.tck.auto.accessories.SpareTire;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
public class JSR330Tck {

    @BootstrapModule
    @Bindings({
            @Bind(type = Car.class, to = Convertible.class),
            @Bind(type = Engine.class , to = V8Engine.class)
    })
    public static class JSR330TckModule {
        @Provides
        @Drivers
        public org.atinject.tck.auto.Seat getDriversSeat(DriversSeat driversSeat){
            return driversSeat;
        }

        @Provides
        @Named("spare")
        private Tire getSpareTire(SpareTire spareTire){
            return spareTire;
        }
    }

    @Factory
    public interface CarFactory{
        Car buildCar();
    }

    public static junit.framework.Test suite(){

        Scopes scopes = new Scopes();
        scopes.addScope(Singleton.class, new ConcurrentDoubleLockingScope());

        Car car = Factories.get(CarFactory.class, scopes).buildCar();

        return Tck.testsFor(car, false, true);
    }
}
