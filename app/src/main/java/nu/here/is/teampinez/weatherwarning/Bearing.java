package nu.here.is.teampinez.weatherwarning;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by max on 5/21/15.
 */
public class Bearing implements SensorEventListener {
    Context context;
    SensorManager sensorManager;
    Integer activeBearing;

    private Sensor sensorMagneticField;
    private Sensor sensorAccelerometer;

    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;

    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;

    public Bearing(Context context) {
        this.context = context;
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private Integer returnAzimuth() {
        float azimuthInDegrees = (float) Math.toDegrees(matrixValues[0]);
        if(azimuthInDegrees < 0.0f) {
            azimuthInDegrees += 360.0f;
            return Math.round(azimuthInDegrees);
        }
        return Math.round(azimuthInDegrees);
    }
}
