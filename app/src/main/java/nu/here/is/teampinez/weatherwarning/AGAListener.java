package nu.here.is.teampinez.weatherwarning;

import android.os.AsyncTask;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.automotiveapi.AutomotiveSignalId;
import android.swedspot.scs.data.SCSFloat;
import android.util.Log;

import com.swedspot.automotiveapi.AutomotiveFactory;
import com.swedspot.automotiveapi.AutomotiveListener;
import com.swedspot.vil.distraction.DriverDistractionLevel;
import com.swedspot.vil.distraction.DriverDistractionListener;
import com.swedspot.vil.distraction.LightMode;
import com.swedspot.vil.distraction.StealthMode;
import com.swedspot.vil.policy.AutomotiveCertificate;

import java.util.HashMap;

/**
 * DATA DATA.
 */
public class AGAListener extends AsyncTask<Object, Void, HashMap<Integer, Object>> {
    HashMap<Integer, Object> things;
    public AGAListener(HashMap<Integer, Object> passedMap) {
        things = passedMap;
    }

    @Override
    protected HashMap<Integer, Object> doInBackground(Object... objects) {
        AutomotiveFactory.createAutomotiveManagerInstance(
                new AutomotiveCertificate(new byte[0]),
                new AutomotiveListener() {
                    @Override
                    public void receive(final AutomotiveSignal automotiveSignal) {
                        if(automotiveSignal.getSignalId() == AutomotiveSignalId.FMS_WHEEL_BASED_SPEED) {
                            things.put(0, ((SCSFloat) automotiveSignal.getData()).getFloatValue());
                        }
                        if(automotiveSignal.getSignalId() == AutomotiveSignalId.FMS_VEHICLE_MOTION) {
                            things.put(1, automotiveSignal.getData());
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
        return things;
    }

    @Override
    protected void onPostExecute(HashMap<Integer, Object> f) {
        Log.e(getClass().getName(), "I AM DONE");
    }
}

