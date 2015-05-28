package nu.here.is.teampinez.weatherwarning;

import nu.here.is.teampinez.weatherwarning.parser.*;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Timer;
import java.util.TimerTask;


public class DriveActivity extends Activity {
    MediaPlayer notificationSound;
    private static DriveActivity instance;
    private boolean muteAlert;

    // Updating things
    LocationHandler locationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_test);

        Switch alertSwitch = (Switch) findViewById(R.id.muteAlerts);
        alertSwitch.setChecked(false);
        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) muteAlert = true;
                else muteAlert = false;
            }
        });

        instance = this;

        locationHandler = new LocationHandler(this);

        notificationSound = MediaPlayer.create(DriveActivity.this, R.raw.notification);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(getClass().getName(), String.valueOf(locationHandler.coordinates.location.getLatitude()));
                Log.d(getClass().getName(), String.valueOf(locationHandler.bearing.activeBearing));
                new ConeParser(DriveActivity.this, findViewById(R.id.driver_view), locationHandler.coordinates.getTriangle(locationHandler.bearing.activeBearing), locationHandler.coordinates.location);

            }
        }, 2000, 30000);
    }
    public static DriveActivity getInstance() {return instance;}

    @Override
    public void onPause() {
        Log.v(getClass().getName(), "Pausing task");
        super.onPause();
        locationHandler.coordinates.locationManager.removeUpdates(locationHandler.coordinates);
        //timer.cancel();
    }

    @Override
    public void onResume() {
        Log.v(getClass().getName(), "Resuming task");
        super.onRestart();
        //timer.schedule(timerTask, 0, 30000);
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

    public boolean getMuteAlert() {
        return muteAlert;
    }
}











