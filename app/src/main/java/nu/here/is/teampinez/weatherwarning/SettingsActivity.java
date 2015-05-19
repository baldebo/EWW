
package nu.here.is.teampinez.weatherwarning;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SettingsActivity extends Activity  {

    Switch toggleNotification;
    NotificationMain notificationMain = new NotificationMain();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);


        /*CheckBox chkBoxFahrenheit, chkBoxCelcius, chkBoxMiles, chkBoxKilometers, chkBoxWindspeed, chkBoxGusts, chkBoxDirection, chkBoxToggleNotifications, chkBoxShowOnLockscreen;

        chkBoxFahrenheit = (CheckBox)findViewById(R.id.chkBoxFahrenheit);
        chkBoxFahrenheit.setChecked(getFromSp("chkBoxFahrenheit"));
        chkBoxFahrenheit.setOnCheckedChangeListener(this);

        chkBoxCelcius = (CheckBox)findViewById(R.id.chkBoxCelcius);
        chkBoxCelcius.setChecked(getFromSp("chkBoxCelcius"));
        chkBoxCelcius.setOnCheckedChangeListener(this);

        chkBoxMiles = (CheckBox)findViewById(R.id.chkBoxMiles);
        chkBoxMiles.setChecked(getFromSp("chkBoxMiles"));
        chkBoxMiles.setOnCheckedChangeListener(this);

        chkBoxKilometers = (CheckBox)findViewById(R.id.chkBoxKilometers);
        chkBoxKilometers.setChecked(getFromSp("chkBoxKilometers"));
        chkBoxKilometers.setOnCheckedChangeListener(this);

        chkBoxWindspeed = (CheckBox)findViewById(R.id.chkBoxWindspeed);
        chkBoxWindspeed.setChecked(getFromSp("chkBoxWindspeed"));
        chkBoxWindspeed.setOnCheckedChangeListener(this);

        chkBoxGusts = (CheckBox)findViewById(R.id.chkBoxGusts);
        chkBoxGusts.setChecked(getFromSp("chkBoxGusts"));
        chkBoxGusts.setOnCheckedChangeListener(this);

        chkBoxDirection = (CheckBox)findViewById(R.id.chkBoxDirection);
        chkBoxDirection.setChecked(getFromSp("chkBoxDirection"));
        chkBoxDirection.setOnCheckedChangeListener(this);

        chkBoxToggleNotifications = (CheckBox)findViewById(R.id.chkBoxToggleNotifications);
        chkBoxToggleNotifications.setChecked(getFromSp("chkBoxToggleNotifications"));
        chkBoxToggleNotifications.setOnCheckedChangeListener(this);

        chkBoxShowOnLockscreen = (CheckBox)findViewById(R.id.chkBoxShowOnLockscreen);
        chkBoxShowOnLockscreen.setChecked(getFromSp("chkBoxShowOnLockscreen"));
        chkBoxShowOnLockscreen.setOnCheckedChangeListener(this);
*/        toggleNotification = (Switch) findViewById(R.id.toggle);
        SharedPreferences sharedPrefs = getSharedPreferences("nu.here.is.team.pinez.weatherwarning", MODE_PRIVATE);
        toggleNotification.setChecked(sharedPrefs.getBoolean("Notification", true));

    }


    public void onClick(View v)
    {
        if (toggleNotification.isChecked())
        {
            SharedPreferences.Editor editor = getSharedPreferences("nu.here.is.team.pinez.weatherwarning", MODE_PRIVATE).edit();
            editor.putBoolean("Notification", true);
            notificationMain.displayNotification();
            editor.apply();
        }
        else
        {
            SharedPreferences.Editor editor = getSharedPreferences("nu.here.is.team.pinez.weatherwarning", MODE_PRIVATE).edit();
            editor.putBoolean("Notification", false);
            notificationMain.stopNotification();
            editor.apply();
        }
    }




/*    private boolean getFromSp(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("SharedPreferences_Test",
                Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    private void saveInSp(String key, boolean value){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("SharedPreferences_Test",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.chkBoxFahrenheit:
                saveInSp("chkBoxFahrenheit", isChecked);
                break;
            case R.id.chkBoxCelcius:
                saveInSp("chkBoxCelcius", isChecked);
                break;
            case R.id.chkBoxMiles:
                saveInSp("chkBoxMiles", isChecked);
                break;
            case R.id.chkBoxKilometers:
                saveInSp("chkBoxKilometers", isChecked);
                break;
            case R.id.chkBoxWindspeed:
                saveInSp("chkBoxWindspeed", isChecked);
                break;
            case R.id.chkBoxGusts:
                saveInSp("chkBoxGusts", isChecked);
                break;
            case R.id.chkBoxDirection:
                saveInSp("chkBoxDirection", isChecked);
                break;
            case R.id.chkBoxToggleNotifications:
                saveInSp("chkBoxToggleNotifications", isChecked);
                break;
            case R.id.chkBoxShowOnLockscreen:
                saveInSp("chkBoxShowOnLockscreen", isChecked);
                break;
        }
    }*/
}

