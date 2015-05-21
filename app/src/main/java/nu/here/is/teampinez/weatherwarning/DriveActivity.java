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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class DriveActivity extends Activity {
    //String stationName[];
    //String airTemp[];
    //String roadTemp[];
    //String windSpd[];
    //String stationCoords[];

    ArrayList<Station> stations = new ArrayList<>();

    TextView txtStationName0;
    TextView txtStationDistance0;
    TextView txtAirTemp0;
    TextView txtRoadtemp0;
    TextView txtWindSpd0;

    TextView txtStationName1;
    TextView txtStationDistance1;
    TextView txtAirTemp1;
    TextView txtRoadtemp1;
    TextView txtWindSpd1;

    TextView txtStationName2;
    TextView txtStationDistance2;
    TextView txtAirTemp2;
    TextView txtRoadtemp2;
    TextView txtWindSpd2;

    MyCurrentLocationListener gps;

    // AverageBearing Variables

    double currentBearing = 0;
    float speed = 0f;
    int averageCounter = 0;
    int counter = 0;
    double calcAverage;
    double avgBearing;
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

        txtStationName0 = (TextView) findViewById(R.id.stationName);
        txtStationDistance0 = (TextView) findViewById(R.id.stationDistance);
        txtAirTemp0 = (TextView) findViewById(R.id.airTemp);
        txtRoadtemp0 = (TextView) findViewById(R.id.roadTemp);
        txtWindSpd0 = (TextView) findViewById(R.id.windSpd);

        txtStationName1 = (TextView) findViewById(R.id.stationName2);
        txtStationDistance1 = (TextView) findViewById(R.id.stationDistance2);
        txtAirTemp1 = (TextView) findViewById(R.id.airTemp2);
        txtRoadtemp1 = (TextView) findViewById(R.id.roadTemp2);
        txtWindSpd1 = (TextView) findViewById(R.id.windSpd2);

        txtStationName2 = (TextView) findViewById(R.id.stationName3);
        txtStationDistance2 = (TextView) findViewById(R.id.stationDistance3);
        txtAirTemp2 = (TextView) findViewById(R.id.airTemp3);
        txtRoadtemp2 = (TextView) findViewById(R.id.roadTemp3);
        txtWindSpd2 = (TextView) findViewById(R.id.windSpd3);

//        getWeather(0);
//        for (int i = 0; i < averageBearing.length; i++) {
//            averageBearing[i] = gps.getBearing();
//        }
        /*new AsyncTask() {
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
        }.execute();*/
//        averageBearing();

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
                            stations = new Parser(DriveActivity.this).execute(1, null, locationHandler.bearing.activeBearing).get();
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

//    public void getWeather(Integer param) {
//        Parser p = new Parser(this);
//
//        /*txtWindSpd = (TextView) findViewById(R.id.txtWindSpd);
//        txtUpdateTime = (TextView) findViewById(R.id.txtUpdateTime);
//        txtFirstBig = (TextView) findViewById(R.id.txtFirstBig);
//        txtWindFrc = (TextView) findViewById(R.id.txtWindFrc);
//        txtTempRoad = (TextView) findViewById(R.id.txtTempRoad);
//        txtTempAir = (TextView) findViewById(R.id.txtTempAir);*/
//
//        try {
//            //TODO Try to make nicer! Perhaps create a method?
//            JSONArray jsonArray = new JSONObject(p.execute(param, 70000).get(1000, TimeUnit.MILLISECONDS)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");
//
//            //Parse data.
//
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                Station s = new Station();
//                JSONObject parsedStation = jsonArray.getJSONObject(i);
//                String coordSplitter = parsedStation.getJSONObject("Geometry").getString("WGS84");
//                coordSplitter = coordSplitter.replace("POINT", "");
//                coordSplitter = coordSplitter.replace("(", "");
//                coordSplitter = coordSplitter.replace(")", "");
//
//                String coordArray[] = coordSplitter.split(" ");
//
//                double statLon = Double.parseDouble(coordArray[1]);
//                double statLat = Double.parseDouble(coordArray[2]);
//                Double statDist = (getDistance(statLat, statLon));
//                s.statDist = statDist;
//
//                // Parse data
//
//                s.name = parsedStation.getString("Name");
//
//                if (parsedStation.getJSONObject("Measurement").getJSONObject("Air").length() == 0) {
//                    s.airTemp = "777";
//                } else {
//                    s.airTemp = parsedStation.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
//                }
//                if (parsedStation.getJSONObject("Measurement").getJSONObject("Road").length() == 0) {
//                    s.roadTemp = "777";
//                } else {
//                    s.roadTemp = parsedStation.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
//                }
//                if (parsedStation.getJSONObject("Measurement").getJSONObject("Wind").length() == 0) {
//                    s.windSpeed = "777";
//                } else {
//                    s.windSpeed = parsedStation.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
//                }
//
//                Log.d("Bob - ", s.name + " - " + String.valueOf(statDist));
//
//                // Catch faulty road temperatures
//
//                try {
//                    if (!s.roadTemp.equals("777")) {
//                        double roadTempDouble = Double.parseDouble(s.roadTemp);
//                        if (roadTempDouble < -50) {
//                            s.roadTemp = "777";
//                        }
//                    }
//                } catch (NumberFormatException e) {
//                    Log.i(getClass().getName(), String.valueOf(e));
//                }
//
//                // Catch faulty air temperatures
//
//                try {
//                    if (!s.airTemp.equals("777")) {
//                        double airTempDouble = Double.parseDouble(s.airTemp);
//                        if (airTempDouble < -50) {
//                            s.airTemp = "777";
//                        }
//                    }
//                } catch (NumberFormatException e) {
//                    Log.i(getClass().getName(), e.toString());
//                }
//
//                // Add object to ArrayList
//                stations.add(s);
//            }
//        } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        String printAll = "";
//        Station temp = new Station();
//        for (int i = 1; i < stations.size(); i++) {
//            printAll += String.valueOf(stations.get(i).statDist);
//            printAll += "\n";
//
//            Collections.sort(stations, new Comparator<Station>() {
//                @Override
//                public int compare(Station c1, Station c2) {
//                    return Double.compare(c1.statDist, c2.statDist);
//                }
//            });
//
//        }
//        for (Station s : stations) {
//            Log.d("Distance", String.valueOf(s.statDist));
//        }
//
//        txtStationName0.setText(stations.get(0).name);
//        txtStationDistance0.setText(String.format("%.1f", stations.get(0).statDist) + " km");
//
//        String notificationString = "";
//        boolean alertFlag = false;
//
//       for (int i = 0; i < stations.size(); i++){
//
//           double numAirTemp = Double.parseDouble(stations.get(i).airTemp);
//           double numRoadTemp = Double.parseDouble(stations.get(i).roadTemp);
//           double numWindSpeed = Double.parseDouble(stations.get(i).windSpeed);
//
//           if (numAirTemp < 3){
//               notificationString += "Air Temperature Low!";
//           }
//           if (numRoadTemp < 3){
//               notificationString += "Road Temperature Low!";
//           }
//           if (numWindSpeed > 15){
//               notificationString += "Wind Speed High!";
//           }
//
//        sendNotification(stations.get(i).name, numAirTemp, numRoadTemp, numWindSpeed);
//
//        }
//
//        Log.d("StationD - 50", stations.get(findStationByDistance(50)).name + " - " + stations.get(findStationByDistance(50)).statDist);
//        Log.d("StationD - 30", stations.get(findStationByDistance(30)).name + " - " + stations.get(findStationByDistance(30)).statDist);
//        Log.d("StationD - 10", stations.get(findStationByDistance(10)).name + " - " + stations.get(findStationByDistance(10)).statDist);
//
//        //sendNotification(stations.get(0).name, 12.2, 12.2, 12.2);
//    }

    public double getDistance(double statLat, double statLon) {
        Log.d("Notification", "Starting getDistance");
        double v = 6372.8;

        double gpsLat = gps.getLatitude(); //lat1
        double gpsLon = gps.getLongitude(); //lon1

        double dLat = Math.toRadians(statLat - gpsLat);
        double dLon = Math.toRadians(statLon - gpsLon);

        gpsLat = Math.toRadians(gpsLat);
        statLat = Math.toRadians(statLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(gpsLat) * Math.cos(statLat);
        double c = 2 * Math.asin(Math.sqrt(a));

        return v * c;
    }

    public void displayBearing(double avgBearing) {
        TextView txtAvgBearing = (TextView) findViewById(R.id.windSpd);
        txtAvgBearing.setText(String.valueOf(avgBearing) + "\u00B0");
        Log.d("Bearing", "------------");
        Log.d("Bearing - 1", String.valueOf(averageBearing[0]));
        Log.d("Bearing - 2", String.valueOf(averageBearing[1]));
        Log.d("Bearing - 3", String.valueOf(averageBearing[2]));
        Log.d("Bearing - 4", String.valueOf(averageBearing[3]));
        Log.d("Bearing - 5", String.valueOf(averageBearing[4]));
//        Log.d("Bearing - C", String.valueOf(gps.getLatitude()));
//        Log.d("Bearing - D", stations.get(1).statDist);
    }

    public void printStations() {
        //getWeather(0);
        if (stations.size() > 2){
            txtStationName0.setText(stations.get(0).name);
            txtStationDistance0.setText(String.format("%.1f", stations.get(0).statDist) + " km");
            txtAirTemp0.setText(stations.get(0).airTemp);
            txtRoadtemp0.setText(stations.get(0).roadTemp);
            txtWindSpd0.setText(stations.get(0).windSpeed);

            txtStationName1.setText(stations.get(1).name);
            txtStationDistance1.setText(String.format("%.1f", stations.get(1).statDist) + " km");
            txtAirTemp1.setText(stations.get(1).airTemp);
            txtRoadtemp1.setText(stations.get(1).roadTemp);
            txtWindSpd1.setText(stations.get(1).windSpeed);
            Log.d("Station 1 - AirTemp ", stations.get(1).airTemp);
            txtStationName2.setText(stations.get(2).name);
            txtStationDistance2.setText(String.format("%.1f", stations.get(2).statDist) + " km");
            txtAirTemp2.setText(stations.get(2).airTemp);
            txtRoadtemp2.setText(stations.get(2).roadTemp);
            txtWindSpd2.setText(stations.get(2).windSpeed);
        }
    }


    public void averageBearing() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getAverageBearingTask startTask = new getAverageBearingTask();
                        startTask.execute();
                    }
                });
            }
        }, 0, 5000);
    }

    private class getAverageBearingTask extends AsyncTask<Double, Void, Double> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gps = new MyCurrentLocationListener(DriveActivity.this);
        }

        @Override
        protected Double doInBackground(Double... args0) {

            if (counter >= 5) {
                counter = 0;
            }

            averageBearing[counter] = gps.getBearing();
            calcAverage = 0;
            for (int i = 0; i < averageBearing.length; i++) {
                if (averageBearing[i] != 0) {
                    averageCounter++;
                    calcAverage += averageBearing[i];
                } else {
                    i++;
                }
            }
            if (averageCounter > 5) {
                averageCounter = 5;
            }

            avgBearing = (calcAverage / averageCounter) % 360;
            counter++;
            return avgBearing;
        }

        @Override
        protected void onPostExecute(Double avgBearing) {
            super.onPostExecute(avgBearing);
            gps = new MyCurrentLocationListener(DriveActivity.this);
            displayBearing(avgBearing);
        }
    }

    public int findStationByDistance(double distance){

        double myDistance = Math.abs(stations.get(0).statDist - distance);
        int iKeeper = 0;

        for (int i = 0; i < stations.size(); i++){
            double iDistance = Math.abs(stations.get(i).statDist - distance);
            if (iDistance < myDistance){
                iKeeper = i;
                myDistance = iDistance;
            }
        }

        return iKeeper;
    }
}











