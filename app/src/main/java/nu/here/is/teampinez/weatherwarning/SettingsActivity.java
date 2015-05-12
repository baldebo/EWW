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


public class SettingsActivity extends ActionBarActivity {
    // Create a constant for the setting that you're saving
    private static final String SETTING_CHECK_BOX = "checkbox_setting";

    private CheckBox chkBoxFahrenheit;
//    private CheckBox chkBoxMiles;
//    private CheckBox chkBoxCelcius;
//    private CheckBox chkBoxKilometers;
//    private CheckBox chkBoxWindspeed;
//    private CheckBox chkBoxGusts;
//    private CheckBox chkBoxDirection;
//    private CheckBox chkBoxToggleNotifications;
//    private CheckBox chkBoxShowOnLockscreen;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
        chkBoxFahrenheit = (CheckBox) findViewById(R.id.chkBoxFahrenheit);
//        chkBoxMiles = (CheckBox) findViewById(R.id.chkBoxMiles);
//        chkBoxCelcius = (CheckBox) findViewById(R.id.chkBoxCelcius);
//        chkBoxKilometers = (CheckBox) findViewById(R.id.chkBoxKilometers);
//        chkBoxWindspeed = (CheckBox) findViewById(R.id.chkBoxWindspeed);
//        chkBoxGusts = (CheckBox) findViewById(R.id.chkBoxGusts);
//        chkBoxDirection = (CheckBox) findViewById(R.id.chkBoxDirection);
//        chkBoxToggleNotifications = (CheckBox) findViewById(R.id.chkBoxToggleNotifications);
//        chkBoxShowOnLockscreen = (CheckBox) findViewById(R.id.chkBoxShowOnLockscreen);


        // Set the initial state of the check box based on saved value
        chkBoxFahrenheit.setChecked(isCheckedSettingEnabled());
//        chkBoxMiles.setChecked(isCheckedSettingEnabled());
//        chkBoxCelcius.setChecked(isCheckedSettingEnabled());
//        chkBoxKilometers.setChecked(isCheckedSettingEnabled());
//        chkBoxWindspeed.setChecked(isCheckedSettingEnabled());
//        chkBoxGusts.setChecked(isCheckedSettingEnabled());
//        chkBoxDirection.setChecked(isCheckedSettingEnabled());
//        chkBoxToggleNotifications.setChecked(isCheckedSettingEnabled());
//        chkBoxShowOnLockscreen.setChecked(isCheckedSettingEnabled());

    }

    @Override
    public void onPause() {
        super.onPause();

        // Persist the setting. Could also do this with an OnCheckedChangeListener.
        setCheckedSettingEnabled(chkBoxFahrenheit.isChecked());
//        setCheckedSettingEnabled(chkBoxMiles.isChecked());
//        setCheckedSettingEnabled(chkBoxCelcius.isChecked());
//        setCheckedSettingEnabled(chkBoxKilometers.isChecked());
//        setCheckedSettingEnabled(chkBoxWindspeed.isChecked());
//        setCheckedSettingEnabled(chkBoxDirection.isChecked());
//        setCheckedSettingEnabled(chkBoxGusts.isChecked());
//        setCheckedSettingEnabled(chkBoxToggleNotifications.isChecked());
//        setCheckedSettingEnabled(chkBoxShowOnLockscreen.isChecked());

    }

    /**
     * Returns true if the setting has been saved as enabled,
     * false by default
     */
    private boolean isCheckedSettingEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SETTING_CHECK_BOX, false);
    }

    /**
     * Persists the new state of the setting
     *
     * @param enabled the new state for the setting
     */
    private void setCheckedSettingEnabled(boolean enabled) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(SETTING_CHECK_BOX, enabled)
                .apply();
    }
}
