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
import org.junit.Before;
import org.junit.Test;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.inject.Qualifier;
import jakarta.inject.Singleton;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Verifies that Transfuse recognizes the jakarta.inject annotations (@Inject, @Singleton, @Named
 * and custom @Qualifier annotations) alongside the javax.inject namespace.
 *
 * @author John Ericksen
 */
public class JakartaInjectTest {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Leather {}

    public interface Engine {}

    @Singleton
    public static class V8Engine implements Engine {}

    public static class Seat {}

    public static class LeatherSeat extends Seat {}

    @Singleton
    public static class Car {
        private final Engine engine;
        private final Seat namedSeat;
        private final Seat qualifiedSeat;
        private final Provider<Seat> seatProvider;
        @Inject
        Seat fieldSeat;
        Seat methodSeat;

        @Inject
        public Car(Engine engine, @Named("driver") Seat namedSeat, @Leather Seat qualifiedSeat, Provider<Seat> seatProvider) {
            this.engine = engine;
            this.namedSeat = namedSeat;
            this.qualifiedSeat = qualifiedSeat;
            this.seatProvider = seatProvider;
        }

        @Inject
        public void setMethodSeat(Seat methodSeat) {
            this.methodSeat = methodSeat;
        }

        public Engine getEngine() {
            return engine;
        }

        public Seat getNamedSeat() {
            return namedSeat;
        }

        public Seat getQualifiedSeat() {
            return qualifiedSeat;
        }

        public Provider<Seat> getSeatProvider() {
            return seatProvider;
        }
    }

    @BootstrapModule
    @Bindings({
            @Bind(type = Engine.class, to = V8Engine.class)
    })
    public static class JakartaModule {
        @Provides
        @Named("driver")
        public Seat getDriverSeat() {
            return new Seat();
        }

        @Provides
        @Leather
        public Seat getLeatherSeat() {
            return new LeatherSeat();
        }
    }

    @Factory
    public interface CarFactory {
        Car buildCar();

        Engine buildEngine();
    }

    private CarFactory factory;

    @Before
    public void setup() {
        //Transfuse canonicalizes @Singleton (javax or jakarta) onto the javax.inject.Singleton scope key
        Scopes scopes = new Scopes().addScope(javax.inject.Singleton.class, new ConcurrentDoubleLockingScope());
        factory = Factories.get(CarFactory.class, scopes);
    }

    @Test
    public void testConstructorInjection() {
        Car car = factory.buildCar();
        assertNotNull(car.getEngine());
        assertTrue(car.getEngine() instanceof V8Engine);
    }

    @Test
    public void testFieldInjection() {
        assertNotNull(factory.buildCar().fieldSeat);
    }

    @Test
    public void testMethodInjection() {
        assertNotNull(factory.buildCar().methodSeat);
    }

    @Test
    public void testNamedQualifier() {
        assertNotNull(factory.buildCar().getNamedSeat());
    }

    @Test
    public void testCustomQualifier() {
        Car car = factory.buildCar();
        assertNotNull(car.getQualifiedSeat());
        assertTrue(car.getQualifiedSeat() instanceof LeatherSeat);
    }

    @Test
    public void testProviderInjection() {
        Provider<Seat> provider = factory.buildCar().getSeatProvider();
        assertNotNull(provider);
        assertNotNull(provider.get());
    }

    @Test
    public void testSingletonScope() {
        Engine one = factory.buildEngine();
        Engine two = factory.buildEngine();
        assertSame(one, two);
    }
}
