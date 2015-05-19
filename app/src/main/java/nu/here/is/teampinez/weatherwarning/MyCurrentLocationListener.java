package nu.here.is.teampinez.weatherwarning;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyCurrentLocationListener implements LocationListener {

    private static final long minDistUpdt = 10;
    private static final long minUpdtTime = 1000 * 60 * 1;
    private final Context context;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double locLat;
    double locLon;
    String address;

    public MyCurrentLocationListener(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            minUpdtTime,
                            minDistUpdt,
                            this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            locLat = location.getLatitude();
                            locLon = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                minUpdtTime,
                                minDistUpdt,
                                this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                locLat = location.getLatitude();
                                locLon = location.getLongitude();
                            }
                        }
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public double getLatitude() {
        if (location != null) {
            locLat = location.getLatitude();
        }
        return locLat;
    }


    public double getLongitude() {
        if (location != null) {
            locLon = location.getLongitude();
        }
        return locLon;
    }

    public double getBearing(){
        double gpsBearing = location.getBearing();
        return gpsBearing;
    }

    public double getAverageBearing(){
        double averageBearing[] = new double[5];
        Log.d("Array Length", String.valueOf(averageBearing.length));
        return averageBearing.length;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    @Override
    public void onLocationChanged(Location loc) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        //print "Currently GPS is Disabled";
    }

    @Override
    public void onProviderEnabled(String provider) {
        //print "GPS got Enabled";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}