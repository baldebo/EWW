package nu.here.is.teampinez.weatherwarning;

import nu.here.is.teampinez.weatherwarning.parser.*;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.automotiveapi.AutomotiveSignalId;
import android.swedspot.scs.data.SCSFloat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.swedspot.automotiveapi.AutomotiveFactory;
import com.swedspot.automotiveapi.AutomotiveListener;
import com.swedspot.vil.distraction.DriverDistractionLevel;
import com.swedspot.vil.distraction.DriverDistractionListener;
import com.swedspot.vil.distraction.LightMode;
import com.swedspot.vil.distraction.StealthMode;
import com.swedspot.vil.policy.AutomotiveCertificate;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class DriveActivity extends Activity {
    MediaPlayer notificationSound;
    private static DriveActivity instance;

    // Updating things
    LocationHandler locationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_test);

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
        }, 2000, 20000);
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

    public void sendNotification(String title, Double airTemperature, Double roadTemperature, Double windSpeed) {
        final int mId = 12345;
        final int PRIORITY_MAX = 2;
        final NotificationCompat.Builder mBuilder;
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] jsonOutput = new String[5];

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText("Nothing dangerous at this time")
                .setOngoing(true);

        //extended notification view
        jsonOutput[0] = "Station";
        inboxStyle.setBigContentTitle("Drag down for more info");

        for (String jsonResult : jsonOutput) {

            inboxStyle.addLine(jsonResult);
            jsonOutput[1] = ("Air Temperature: " + airTemperature);
            jsonOutput[2] = ("Road Temperature: " + roadTemperature);
            jsonOutput[3] = ("Wind Speed: " + windSpeed);
        }

        mBuilder.setStyle(inboxStyle);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, DriveActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(GUIActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT

                );

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(mId, mBuilder.build());
        mBuilder.setOngoing(true);

    }


    public void displayBearing(double avgBearing) {
        TextView txtAvgBearing = (TextView) findViewById(R.id.windSpd);
        txtAvgBearing.setText(String.valueOf(avgBearing) + "\u00B0");
    }


//    public void averageBearing() {
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getAverageBearingTask startTask = new getAverageBearingTask();
//                        startTask.execute();
//                    }
//                });
//            }
//        }, 0, 5000);
//    }
//
//    private class getAverageBearingTask extends AsyncTask<Double, Void, Double> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            gps = new MyCurrentLocationListener(DriveActivity.this);
//        }
//
//        @Override
//        protected Double doInBackground(Double... args0) {
//
//            if (counter >= 5) {
//                counter = 0;
//            }
//
//            averageBearing[counter] = gps.getBearing();
//            calcAverage = 0;
//            for (int i = 0; i < averageBearing.length; i++) {
//                if (averageBearing[i] != 0) {
//                    averageCounter++;
//                    calcAverage += averageBearing[i];
//                } else {
//                    i++;
//                }
//            }
//            if (averageCounter > 5) {
//                averageCounter = 5;
//            }
//
//            avgBearing = (calcAverage / averageCounter) % 360;
//            counter++;
//            return avgBearing;
//        }
//
//        @Override
//        protected void onPostExecute(Double avgBearing) {
//            super.onPostExecute(avgBearing);
//            gps = new MyCurrentLocationListener(DriveActivity.this);
//            displayBearing(avgBearing);
//        }
//    }
}











