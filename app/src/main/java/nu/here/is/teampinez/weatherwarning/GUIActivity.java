package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.automotiveapi.AutomotiveSignalId;
import android.swedspot.scs.SCSFactory;
import android.swedspot.scs.data.SCSFloat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.swedspot.automotiveapi.AutomotiveFactory;
import com.swedspot.automotiveapi.AutomotiveListener;
import com.swedspot.vil.distraction.DriverDistractionLevel;
import com.swedspot.vil.distraction.DriverDistractionListener;
import com.swedspot.vil.distraction.LightMode;
import com.swedspot.vil.distraction.StealthMode;
import com.swedspot.vil.policy.AutomotiveCertificate;

public class GUIActivity extends Activity {

    TextView theTextView;
    MyCurrentLocationListener gps;

    /*
    Variables below this point are used to store coordinates.
     */
    double locLat;
    double locLon;
    int locLatInt;
    int locLonInt;
    float speed = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);
        gps = new MyCurrentLocationListener(GUIActivity.this);
        if (gps.canGetLocation()) {
            updateCoordinates();
            convertCoordsToInt();
        }

        //ImageButton ourButton = (ImageButton) findViewById(R.id.imgBtnInformation);
        /*ourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gps.canGetLocation()) {
                    updateCoordinates();
                    convertCoordsToInt();
                    theTextView.setText("Your location is\nLat: " + locLatInt + "\nLong: " + locLonInt);
                }
            }
        });*/

        theTextView = (TextView) findViewById(R.id.textHeader);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... objects) {
                AutomotiveFactory.createAutomotiveManagerInstance(
                        new AutomotiveCertificate(new byte[0]),
                        new AutomotiveListener() {
                            @Override
                            public void receive(final AutomotiveSignal automotiveSignal) {
                                speed = ((SCSFloat) automotiveSignal.getData()).getFloatValue();
                                if (speed >= 1) {startDriveView();}
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
                            public void levelChanged(final DriverDistractionLevel driverDistractionLevel) {
                            }

                            @Override
                            public void lightModeChanged(LightMode lightMode) {
                            }

                            @Override
                            public void stealthModeChanged(StealthMode stealthMode) {
                            }
                        }
                ).register(AutomotiveSignalId.FMS_WHEEL_BASED_SPEED);
                return null;
            }
        }.execute();


}
    public void startDriveView() {
            Intent intent = new Intent(this, DriveActivity.class);
            startActivity(intent);
    }


    /**
     * These methods facilitate changes between activities
     **/

    public void openMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void openStationView(View view) {
        Intent intent = new Intent(this, StationActivity.class);
        startActivity(intent);
    }

    public void openDriveView(View view) {
        Intent intent = new Intent(this, DriveActivity.class);
        startActivity(intent);
    }

    /**
     Coordinates methods here below.
     **/

    public void updateCoordinates() {

        /**
         * This pulls coordinates from the GPS Locator
         */

        locLat = gps.getLatitude();
        locLon = gps.getLongitude();
    }

    public void convertCoordsToInt() {

        /**
         * This converts coordinates into a format accepted by the parser.
         */

        locLatInt = (int) locLat * 100000;
        locLonInt = (int) locLon * 100000;
    }


}
