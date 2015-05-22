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

import java.util.ArrayList;

/**
 * Created by max on 5/22/15.
 */
public class AGAListener extends AsyncTask<Object, Void, ArrayList<Float>> {
    ArrayList<Float> things;
    public AGAListener(ArrayList<Float> passedArray) {
        things = passedArray;
    }

    @Override
    protected ArrayList<Float> doInBackground(Object... objects) {
        AutomotiveFactory.createAutomotiveManagerInstance(
                new AutomotiveCertificate(new byte[0]),
                new AutomotiveListener() {
                    @Override
                    public void receive(final AutomotiveSignal automotiveSignal) {
//                        new Runnable() {
//                            @Override
//                            public void run() {
                                things.set(0,(((SCSFloat) automotiveSignal.getData()).getFloatValue()));
//                            }
//                        };
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
        ).register(AutomotiveSignalId.FMS_WHEEL_BASED_SPEED);
        return things;
    }

    @Override
    protected void onPostExecute(ArrayList<Float> f) {
        Log.e(getClass().getName(), "I AM DONE");
    }
}

