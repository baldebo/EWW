package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
//  MAP IMPORTS //
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class GUIActivity extends Activity {

    TextView theTextView;
    MyCurrentLocationListener gps;

    double locLat;
    double locLon;
    double locLonTemp;
    double locLatTemp;
    int locLatInt;
    int locLonInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);
        gps = new MyCurrentLocationListener(GUIActivity.this);
        if(gps.canGetLocation()){
            updateCoordinates();
        }
        ImageButton ourButton = (ImageButton) findViewById(R.id.imgBtnInformation);
        ourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps.canGetLocation()) {
                    updateCoordinates();
                    convertCoordsToInt();
                    theTextView.setText("Your location is\nLat: " + locLatInt + "\nLong: " + locLonInt);
                }
            }
        });

        theTextView = (TextView) findViewById(R.id.textHeader);

        //NOTIFICATION CODE
        final int mId = 12345;
        final int PRIORITY_MAX = 2;
        final NotificationCompat.Builder mBuilder;

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] jsonOutput = new String[4];

        Parser p = new Parser(GUIActivity.this);
        String[] stationName = new String[0];
        String[] airTemp = new String[0];
        String[] roadTemp = new String[0];
        String[] windSpd = new String[0];


        try {
            //TODO Try to make nicer!
            JSONArray jsonArray = new JSONObject(p.execute().get(1000, TimeUnit.MILLISECONDS)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");

            Log.d("JSON", jsonArray.toString());
            stationName = new String[jsonArray.length()];
            airTemp = new String[jsonArray.length()];
            roadTemp = new String[jsonArray.length()];
            windSpd = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                stationName[i] = station.getString("Name");
                airTemp[i] = station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                roadTemp[i] = station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                windSpd[i] = station.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");

                Log.d("JSON Station > ", station.getString("Name"));
                Log.d("JSON Air Temp > ", station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp"));
                Log.d("JSON Road Temp > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
                Log.d("JSON Wind Force > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));

            }
            jsonOutput[0] = ("Name: " + stationName[0]);
            jsonOutput[1] = ("Air Temperature: " + airTemp[0] + "C");
            jsonOutput[2] = ("Road Temperature: " + roadTemp[0] + "C");
            jsonOutput[3] = ("Wind Speed: " + windSpd[0] + " m/s");

        } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        //Air Temp
        String airTemperature;
        if(airTemp[0] != null) {airTemperature = Double.toString(Double.parseDouble(airTemp[0])) + "C";}
        else {airTemperature = "Not available!";}


        String windSpeed;
        if(windSpd[0] != null) {windSpeed = Double.toString(Double.parseDouble(windSpd[0])) + "M/S";}
        else{windSpeed = "Not available!";}

        String roadTemperature;
        if(roadTemp[0] != null) {roadTemperature = Double.toString(Double.parseDouble(roadTemp[0])) + "C";}
        else {roadTemperature = "Not available!";}

        String stnName;
        if(stationName[0] != null) {stnName = stationName[0];}
        else {stnName = "Not Available!";}

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_warning)
                .setContentTitle(jsonOutput[0])
                .setContentText("Drag Down For More Info!")
                .setOngoing(true)
                .setColor(0xd903);

        inboxStyle.setBigContentTitle(jsonOutput[0]);
        for (String jsonResult : jsonOutput) {
            inboxStyle.addLine(jsonResult);
            jsonOutput[0] = ("Name: " + stnName);
            jsonOutput[1] = ("Air Temperature: " + airTemperature);
            jsonOutput[2] = ("Road Temperature: " + roadTemperature);
            jsonOutput[3] = ("Wind Speed: " + windSpeed);
        }
        mBuilder.setStyle(inboxStyle);

        //To quantify the metrics that get pulled as Strings we parse them into ints then check if they are extreme,
        // if so then the notification vibrates, and the LED is red and plays a sound to warn the user, also now the display lights up

        double rTemp;
        if (roadTemp[0] != null) {rTemp = Double.parseDouble(roadTemp[0]);}
        else {rTemp = 2;}

        double aTemp;
        if (airTemp[0] != null) {aTemp = Double.parseDouble(airTemp[0]);}
        else {aTemp = 2;}


        double wSpd;
        if (windSpd[0] != null ) {wSpd  = Double.parseDouble(windSpd[0]);}
        else {wSpd = 0;}

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (rTemp <= 0 || aTemp >= 10 || wSpd >= 2) {
                mBuilder.setColor(16711680).setSound(uri).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.RED, 3000, 3000).setOngoing(true);
                PowerManager.WakeLock screenOn = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "example");
                screenOn.acquire();
                screenOn.release();

            }


        //Set to Top Priority because it involves weather emergency warnings (2 is the highest priority)
        mBuilder.setPriority(PRIORITY_MAX);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, GUIActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationMain.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT

                );

        mBuilder.setContentIntent(resultPendingIntent);
        final NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }


    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void openHelp(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void openFavorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void updateCoordinates() {
        locLat = gps.getLatitude();
        locLon = gps.getLongitude();
    }

    public void convertCoordsToInt() {
        locLonTemp = locLon * 100000;
        locLatTemp = locLat * 100000;
        locLatInt = (int) locLatTemp;
        locLonInt = (int) locLonTemp;
    }


}
