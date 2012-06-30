package org.androidtransfuse.integrationTest.inject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
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

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(context, buildLocationString(location), ONE_SECOND).show();
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
