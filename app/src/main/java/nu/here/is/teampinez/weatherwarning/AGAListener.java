package nu.here.is.teampinez.weatherwarning;

import android.os.AsyncTask;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.automotiveapi.AutomotiveSignalId;
import android.swedspot.scs.data.SCSFloat;
import android.swedspot.scs.data.Uint8;
import android.util.Log;

import com.swedspot.automotiveapi.AutomotiveFactory;
import com.swedspot.automotiveapi.AutomotiveListener;
import com.swedspot.vil.distraction.DriverDistractionLevel;
import com.swedspot.vil.distraction.DriverDistractionListener;
import com.swedspot.vil.distraction.LightMode;
import com.swedspot.vil.distraction.StealthMode;
import com.swedspot.vil.policy.AutomotiveCertificate;

/**
 * DATA DATA.
 */
public class AGAListener extends AsyncTask<Object, Void, Void> {

    @Override
    protected Void doInBackground(Object... objects) {
        AutomotiveFactory.createAutomotiveManagerInstance(
                new AutomotiveCertificate(new byte[0]),
                new AutomotiveListener() {
                    @Override
                    public void receive(final AutomotiveSignal automotiveSignal) {
                        if(automotiveSignal.getSignalId() == AutomotiveSignalId.FMS_WHEEL_BASED_SPEED) {
                            AGAValues.SPEED = ((SCSFloat) automotiveSignal.getData()).getFloatValue();
                        }
                        if(automotiveSignal.getSignalId() == AutomotiveSignalId.FMS_VEHICLE_MOTION) {
                            AGAValues.IN_MOTION = ((Uint8) automotiveSignal.getData()).getIntValue();
                        }
                    }

                    @Override
                    public void timeout(int i) {

                    }

                    @Override
                    public void notAllowed(int i) {

                    }
                },
                new DriverDistractionListener() {
                    @Override
                    public void levelChanged(DriverDistractionLevel driverDistractionLevel) {

                    }

                    @Override
                    public void lightModeChanged(LightMode lightMode) {

                    }

                    @Override
                    public void stealthModeChanged(StealthMode stealthMode) {

                    }
                }
        ).register(AutomotiveSignalId.FMS_WHEEL_BASED_SPEED, AutomotiveSignalId.FMS_VEHICLE_MOTION);

        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        Log.e(getClass().getName(), "I AM DONE");
    }
}

