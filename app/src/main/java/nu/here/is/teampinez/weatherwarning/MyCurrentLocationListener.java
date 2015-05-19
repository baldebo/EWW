package nu.here.is.teampinez.weatherwarning;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;


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

    public Coordinate getLoc() {
        Coordinate c = new Coordinate();
        c.lat = getLatitude();
        c.lon = getLongitude();

        return c;
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

    ArrayList<Coordinate> getTriangle() {
        ArrayList<Coordinate> c = new ArrayList<>();

        Coordinate coord1 = new Coordinate();
        coord1.lat = getLatitude();
        coord1.lon = getLongitude();

        Coordinate coord2, coord3;
        coord2 = distCoord(coord1, getBearing() - 10.0);
        coord3 = distCoord(coord1, getBearing() + 10.0);

        c.add(coord1);
        c.add(coord2);
        c.add(coord3);

        Log.d(getClass().getName()+" Coord1", String.valueOf(c.get(0).lat));
        Log.d(getClass().getName()+" Coord1", String.valueOf(c.get(0).lon));

        Log.d(getClass().getName()+" Coord2", String.valueOf(c.get(1).lat));
        Log.d(getClass().getName()+" Coord2", String.valueOf(c.get(1).lon));

        Log.d(getClass().getName()+" Coord3", String.valueOf(c.get(2).lat));
        Log.d(getClass().getName()+" Coord3", String.valueOf(c.get(2).lon));

        return c;
    }

    private Coordinate distCoord(Coordinate c, double b) {
        Coordinate newc = new Coordinate();
        double dist = 40.0/6371.0;
        double brng = Math.toRadians(b);
        double lat1 = Math.toRadians(c.lat);
        double lon1 = Math.toRadians(c.lon);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) + Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
        double a = Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1), Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));
        double lon2 = lon1 + a;

        lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;

        newc.lat = Math.toDegrees(lat2);
        newc.lon = Math.toDegrees(lon2);

        return newc;
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

final class Coordinate {
    double lat;
    double lon;
}