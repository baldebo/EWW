package nu.here.is.teampinez.weatherwarning;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;
import com.github.goober.coordinatetransformation.positions.WGS84Position;

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

    ArrayList<SWEREF99Position> getTriangle() {
        ArrayList<SWEREF99Position> pos = new ArrayList<>();

        WGS84Position[] positions = new WGS84Position[4];
        positions[0] = new WGS84Position();
        positions[1] = new WGS84Position();
        positions[2] = new WGS84Position();
        positions[3] = new WGS84Position();

        positions[0].setLatitudeFromString(String.valueOf(getLatitude()), WGS84Position.WGS84Format.Degrees);
        positions[0].setLongitudeFromString(String.valueOf(getLongitude()), WGS84Position.WGS84Format.Degrees);

        positions[1] = distantPos(positions[0], getBearing() - 10.0);
        positions[2] = distantPos(positions[0], getBearing());
        positions[3] = distantPos(positions[0], getBearing() + 10.0);

        for(WGS84Position p : positions) {
            pos.add(new SWEREF99Position(p, SWEREF99Position.SWEREFProjection.sweref_99_tm));
        }

        for(SWEREF99Position p : pos) {
            Log.d(getClass().getName(), p.toString());
        }

        return pos;
    }

    private WGS84Position distantPos(WGS84Position p, double bearing) {
        WGS84Position newpos = new WGS84Position();

        double dist = 40.0/6371.0;
        bearing = Math.toRadians(bearing);
        double lat1 = Math.toRadians(p.getLatitude());
        double lon1 = Math.toRadians(p.getLongitude());

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) + Math.cos(lat1) * Math.sin(dist) * Math.cos(bearing));
        double a = Math.atan2(Math.sin(bearing) * Math.sin(dist) * Math.cos(lat1), Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));
        double lon2 = lon1 + a;
        lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;

        newpos.setLatitudeFromString(String.valueOf(Math.toDegrees(lat2)), WGS84Position.WGS84Format.Degrees);
        newpos.setLongitudeFromString(String.valueOf(Math.toDegrees(lon2)), WGS84Position.WGS84Format.Degrees);

        return newpos;
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