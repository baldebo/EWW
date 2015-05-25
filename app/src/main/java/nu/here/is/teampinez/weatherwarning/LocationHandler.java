package nu.here.is.teampinez.weatherwarning;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;
import com.github.goober.coordinatetransformation.positions.WGS84Position;

import java.util.ArrayList;

/**
 * LocationHandler
 * Contains Bearing and Location classes.
 */
public class LocationHandler {
    Bearing bearing;
    Coordinates coordinates;

    LocationHandler(Context context) {
        bearing = new Bearing(context);
        coordinates = new Coordinates(context);

    }

    /**
     * Bearing class
     */
    class Bearing implements SensorEventListener {
        SensorManager sensorManager;
        Integer activeBearing;

        private Sensor sensorMagneticField;
        private Sensor sensorAccelerometer;

        private float[] valuesAccelerometer;
        private float[] valuesMagneticField;

        private float[] matrixR;
        private float[] matrixI;
        private float[] matrixValues;

        /**
         * Bearing Constructor
         * 
         * @param context Context to bind to.
         */
        Bearing(Context context) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            valuesAccelerometer = new float[3];
            valuesMagneticField = new float[3];

            matrixR = new float[9];
            matrixI = new float[9];
            matrixValues = new float[3];

            sensorManager.registerListener(this, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        /**
         * @see SensorEventListener
         */
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(event.values, 0, valuesMagneticField, 0, 3);
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, valuesAccelerometer, 0, 3);
                    break;
            }

            boolean success = SensorManager.getRotationMatrix(
                    matrixR,
                    matrixI,
                    valuesAccelerometer,
                    valuesMagneticField
            );

            if(success) {
                SensorManager.getOrientation(matrixR, matrixValues);
            }

            activeBearing = returnAzimuth();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

        /**
         * returnAzimuth
         *
         * @return Azimuth in Degrees.
         */
        private Integer returnAzimuth() {
            float azimuthInDegrees = (float) Math.toDegrees(matrixValues[0]);
            if(azimuthInDegrees < 0.0f) {
                azimuthInDegrees += 360.0f;
                return Math.round(azimuthInDegrees);
            }
            return Math.round(azimuthInDegrees);
        }
    }

    /**
     * Coordinates class
     */
    class Coordinates implements LocationListener {

        LocationManager locationManager;
        Location location;

        Coordinates(Context context) {
            String provider;
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setBearingRequired(false);
            criteria.setAltitudeRequired(false);

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            provider = locationManager.getBestProvider(criteria, true);

            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 1L, 1f, this);
        }

        /**
         * @see LocationListener
         */
        @Override
        public void onLocationChanged(Location loc) {
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(getClass().getName(), longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(getClass().getName(), latitude);

            location = loc;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

        /**
         * Transforms a WGS84 coordinate to SWEREF99 Coordinates
         *
         * @return Transformed position
         */
        public SWEREF99Position getLoc() {
            return new SWEREF99Position(new WGS84Position(location.getLatitude(), location.getLongitude()), SWEREF99Position.SWEREFProjection.sweref_99_tm);
        }

        /**
         * Calculates a "cone" of using current location.
         *
         * @param bearing Magnetic heading
         * @return ArrayList with three SWEREF99Positions
         */
        ArrayList<SWEREF99Position> getTriangle(Integer bearing) {
            ArrayList<SWEREF99Position> pos = new ArrayList<>();
            WGS84Position[] wgs84Positions = new WGS84Position[3];
            wgs84Positions[0] = new WGS84Position();
            wgs84Positions[1] = new WGS84Position();
            wgs84Positions[2] = new WGS84Position();

            wgs84Positions[0].setLatitudeFromString(String.valueOf(location.getLatitude()), WGS84Position.WGS84Format.Degrees);
            wgs84Positions[0].setLongitudeFromString(String.valueOf(location.getLongitude()), WGS84Position.WGS84Format.Degrees);

            wgs84Positions[1] = distantPos(wgs84Positions[0], bearing - 20.0);
            wgs84Positions[2] = distantPos(wgs84Positions[0], bearing + 20.0);

            for(WGS84Position p : wgs84Positions) {
                pos.add(new SWEREF99Position(p, SWEREF99Position.SWEREFProjection.sweref_99_tm));
            }
            for(SWEREF99Position p : pos) {
                Log.d(getClass().getName(), p.toString());
                Log.d(getClass().getName(), String.valueOf(p.getLatitude()));
            }

            return pos;

        }

        /**
         * Returns a position 80km away from current position using the current bearing
         *
         * @param p Base Position
         * @param bearing Magnetic Heading
         * @return Position 80km away from current position.
         */
        private WGS84Position distantPos(WGS84Position p, double bearing) {
            WGS84Position newpos = new WGS84Position();

            /**
             * TODO
             * Calculate relevant distance using AGAValues.SPEED
             */
            double dist = 80.0/6371.0;
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
    }
}
