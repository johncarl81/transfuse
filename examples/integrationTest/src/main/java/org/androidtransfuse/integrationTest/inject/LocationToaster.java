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
package org.androidtransfuse.integrationTest.inject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
public class LocationToaster implements LocationListener{

    @Inject
    private Context context;

    @Inject
    private LocationManager locationManager;

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(context, buildLocationString(location), ONE_SECOND).show();

        locationManager.removeUpdates(this);
    }

    private String buildLocationString(Location location) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("Lat: ");
        buffer.append(location.getLatitude());
        buffer.append("Long: ");
        buffer.append(location.getLongitude());

        return buffer.toString();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
