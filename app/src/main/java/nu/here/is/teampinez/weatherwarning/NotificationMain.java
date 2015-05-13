package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NotificationMain extends ActionBarActivity {

    //Notification n;
    NotificationCompat.Builder notification;
    public static final int mId = 12345;
    public static final int PRIORITY_MAX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);

        notification = new NotificationCompat.Builder(this);
        //n.flags = Notification.FLAG_NO_CLEAR;

        final NotificationCompat.Builder mBuilder;

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] jsonOutput = new String[4];

        Parser p = new Parser(NotificationMain.this);
        String[] stationName = new String[0];

        try {
            //TODO Try to make nicer!
            JSONArray jsonArray = new JSONObject(p.execute().get(1000, TimeUnit.MILLISECONDS)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");

            Log.d("JSON", jsonArray.toString());
            stationName = new String[jsonArray.length()];
            final String airTemp[] = new String[jsonArray.length()];
            final String roadTemp[] = new String[jsonArray.length()];
            final String windSpd[] = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                stationName[i] = station.getString("Name");
                airTemp[i] = station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                roadTemp[i] = station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                windSpd[i] = station.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                //Log.d("JSON Station >", station.getString("Name"));

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

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_warning)
                .setContentTitle("Name: " + stationName[0])
                .setContentText("Hello World!");

        //mBuilder.setDefaults(Notification.DEFAULT_SOUND
        //        | Notification.DEFAULT_LIGHTS | Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT);



        //jsonOutput[0] = "";
        //jsonOutput[1] = "";
        //jsonOutput[2] = "";
        //jsonOutput[3] = "";
        inboxStyle.setBigContentTitle("FORECAST:");
        for (String jsonResult : jsonOutput) {
            inboxStyle.addLine(jsonResult);
        }
        mBuilder.setStyle(inboxStyle);

        //Set to Top Priority because it involves weather emergency warnings (2 is the highest priority)
        mBuilder.setPriority(PRIORITY_MAX);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationMain.class);

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
        //notification.setOngoing(true);


        //boolean permanent = PreferenceManager.getDefaultSharedPreferences().getBoolean("permanent", false);
        //if(permanent) {
        //    mBuilder.setOngoing(true);
        //}


    }

    }

