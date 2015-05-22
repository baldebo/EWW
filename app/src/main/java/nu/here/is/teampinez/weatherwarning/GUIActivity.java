package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GUIActivity extends Activity {

    HashMap<Integer, Object> data = new HashMap<>();

    /*
    Variables below this point are used to store coordinates.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);

        new AGAListener(data).execute();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.e(getClass().getName(), String.valueOf(data.get(0)));
                    Log.e(getClass().getName(), String.valueOf(data.get(1)));
                } catch (NullPointerException e) {
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
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
}
