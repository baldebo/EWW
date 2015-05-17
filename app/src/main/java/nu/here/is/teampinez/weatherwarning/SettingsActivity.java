package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ToggleButton;


public class SettingsActivity extends Activity {

    NotificationMain notificationMain;
    Button ToggleChanges;
    public static final int SETTINGS_INFO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        ToggleChanges = (Button) findViewById(R.id.btnToggleChanges);
        ToggleChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPreference = new Intent(getApplicationContext(),
                        SettingPreference.class);
                startActivityForResult(intentPreference, SETTINGS_INFO);
            }
        });
    }

    @Override
    //Save settings when system kills app, or system crashes
    protected void onSaveInstanceState(Bundle outState) {
        outState.getBoolean("ToggleNotification", false);
        super.onSaveInstanceState(outState);
    }

    //save settings when USER kills app
    private void saveSettings(){
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putBoolean("ToggleNotification", false);
        editor.apply();
    }

    //onStop = user kills app, so we save settings once we detect user killed the app
    @Override
    protected void onStop() {
        saveSettings();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_INFO){
            //updateNotificationActivation();
        }
    }

    /*private void updateNotificationActivation(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.getBoolean("ToggleNotification", true)){
            notificationMain.displayNotification(view);
            notificationMain.mNotificationManager.notify(notificationMain.mId, notificationMain.mBuilder.build());
            Toast.makeText(this, "Notification Activated",
                    Toast.LENGTH_SHORT).show();

        }
        else {
            notificationMain.mNotificationManager.cancel(notificationMain.mId);
            Toast.makeText(this, "Please Check Notification Toggle to Make the Notification Appear",
                    Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
