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
package org.androidtransfuse;

import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.Bindings;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.annotations.Provides;
import org.androidtransfuse.bootstrap.BootstrapModule;
import org.androidtransfuse.scope.ConcurrentDoubleLockingScope;
import org.androidtransfuse.scope.Scopes;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.Drivers;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.Tire;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.SpareTire;

import jakarta.inject.Named;

/**
 * Runs the official jakarta.inject JSR-330 TCK against the Transfuse injection engine.
 *
 * @author John Ericksen
 */
public class JakartaTckTest {

    @BootstrapModule
    @Bindings({
            @Bind(type = Car.class, to = Convertible.class),
            @Bind(type = Engine.class, to = V8Engine.class)
    })
    public static class JakartaTckModule {
        @Provides
        @Drivers
        public org.atinject.tck.auto.Seat getDriversSeat(DriversSeat driversSeat) {
            return driversSeat;
        }

        @Provides
        @Named("spare")
        public Tire getSpareTire(SpareTire spareTire) {
            return spareTire;
        }
    }

    @Factory
    public interface CarFactory {
        Car buildCar();
    }

    public static junit.framework.Test suite() {

        //Transfuse canonicalizes @Singleton (javax or jakarta) onto the javax.inject.Singleton scope key
        Scopes scopes = new Scopes().addScope(javax.inject.Singleton.class, new ConcurrentDoubleLockingScope());

        Car car = Factories.get(CarFactory.class, scopes).buildCar();

        return Tck.testsFor(car, false, true);
    }
}
