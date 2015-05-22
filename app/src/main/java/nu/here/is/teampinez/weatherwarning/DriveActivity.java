package nu.here.is.teampinez.weatherwarning;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class DriveActivity extends Activity {
    ArrayList<Station> stations = new ArrayList<>();

    // AverageBearing Variables

    float speed = 0f;
    Double averageBearing[] = new Double[5];
    MediaPlayer notificationSound;

    // Updating things
    Handler handler;
    LocationHandler locationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationHandler = new LocationHandler(this);
        setContentView(R.layout.driver_test);

        notificationSound = MediaPlayer.create(DriveActivity.this, R.raw.notification);

        // Assign TextViews


        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... objects) {
                AutomotiveFactory.createAutomotiveManagerInstance(
                        new AutomotiveCertificate(new byte[0]),
                        new AutomotiveListener() {
                            @Override
                            public void receive(final AutomotiveSignal automotiveSignal) {
                                speed = ((SCSFloat) automotiveSignal.getData()).getFloatValue();
                            }

                            @Override
                            public void timeout(int i) {
                            }

                            @Override
                            public void notAllowed(int i) {
                            }
                        },
                        new DriverDistractionListener() {
                            @Override
                            public void levelChanged(final DriverDistractionLevel driverDistractionLevel) {
                            }

                            @Override
                            public void lightModeChanged(LightMode lightMode) {
                            }

                            @Override
                            public void stealthModeChanged(StealthMode stealthMode) {
                            }
                        }
                ).register(AutomotiveSignalId.FMS_WHEEL_BASED_SPEED);
                return null;
            }
        }.execute();

        /**
         * This is for updating every something minutes
         */
        handler = new Handler();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stations = new Parser(DriveActivity.this, findViewById(R.id.driver_view)).execute(1, null, locationHandler.bearing.activeBearing).get();

                            //stations = new Parser(DriveActivity.this).execute(0,100000,null).get();
                            // Commented out for now, gives NullPointerException if not running AGA.
                            //if (speed >= 50) {
                            //} else {
                            //    stations = new Parser(DriveActivity.this).execute(0, 7500, null).get();
                            //}
                            //sendNotification(stations.get(0).name, Double.parseDouble(stations.get(0).airTemp), Double.parseDouble(stations.get(0).roadTemp), Double.parseDouble(stations.get(0).windSpeed));

                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        // 5 minutes, Milliseconds?
        //timer.schedule(timerTask, 0, 300000);
        timer.schedule(timerTask, 0, 30000);
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











