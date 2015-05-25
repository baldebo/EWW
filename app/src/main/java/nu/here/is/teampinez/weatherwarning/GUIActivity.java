package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.swedspot.scs.data.Uint8;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GUIActivity extends Activity {

    private HashMap<String, Object> data = new HashMap<>();
    private AGAValues agav = new AGAValues();

    /*
    Variables below this point are used to store coordinates.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);

        new AGAListener().execute();

//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Log.d(getClass().getName(), String.valueOf(AGAValues.IN_MOTION));
//                if(AGAValues.IN_MOTION == 0) {
//                    Log.d(getClass().getName(), "STANDING STILL");
//                } else {
//                    Log.d(getClass().getName(), "MOVING TRUCK");
//                }
//            }
//        };
//        timer.schedule(timerTask, 0, 1000);
    }

    public void startDriveView() {
        if(AGAValues.IN_MOTION == 1) {
            Toast.makeText(getApplicationContext(), "sorry u driving", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, DriveActivity.class);
            startActivity(intent);
        }
    }

    protected void onPause() {
        super.onPause();
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
        if(AGAValues.IN_MOTION == 1) {
            Toast.makeText(getApplicationContext(), "sorry u driving", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, DriveActivity.class);
            startActivity(intent);
        }
    }
}
