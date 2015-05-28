package nu.here.is.teampinez.weatherwarning;

import nu.here.is.teampinez.weatherwarning.parser.*;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Timer;
import java.util.TimerTask;


public class DriveActivity extends Activity {
    private MediaPlayer notificationSound;
    private Vibrator vibrator;
    private Timer alarmTimer;
    private boolean muteAlert;

    // Updating things
    LocationHandler locationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_test);

        locationHandler = new LocationHandler(this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        notificationSound = MediaPlayer.create(DriveActivity.this, R.raw.notification);

        Switch alertSwitch = (Switch) findViewById(R.id.muteAlerts);
        alertSwitch.setChecked(false);
        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmTimer.cancel();
                } else {
                    alarmTimer = new Timer();
                    alarmTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //TODO Same function as below.
                            // With the same interval.
                            vibrator.vibrate(500);
                        }
                    }, 500, 1000);
                }
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(getClass().getName(), String.valueOf(locationHandler.coordinates.location.getLatitude()));
                Log.d(getClass().getName(), String.valueOf(locationHandler.bearing.activeBearing));
                new ConeParser(DriveActivity.this, findViewById(R.id.driver_view), locationHandler.coordinates.getTriangle(locationHandler.bearing.activeBearing), locationHandler.coordinates.location);

            }
        }, 2000, 30000);

        alarmTimer = new Timer();
        alarmTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                /**
                 * TODO Check for alarm boolean
                 * Set a nice interval.
                 *
                 * Solution, Create a public static boolean to access from the ConeParser?
                 **/
                vibrator.vibrate(500);
            }
        }, 500, 1000);
    }

    @Override
    public void onPause() {
        Log.v(getClass().getName(), "Pausing task");
        super.onPause();
        locationHandler.coordinates.locationManager.removeUpdates(locationHandler.coordinates);
        alarmTimer.cancel();
    }

    @Override
    public void onResume() {
        Log.v(getClass().getName(), "Resuming task");
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}











