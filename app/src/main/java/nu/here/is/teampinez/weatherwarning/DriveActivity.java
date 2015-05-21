package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.automotiveapi.AutomotiveSignalId;
import android.swedspot.scs.data.SCSFloat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.drive.Drive;
import com.swedspot.automotiveapi.AutomotiveFactory;
import com.swedspot.automotiveapi.AutomotiveListener;
import com.swedspot.vil.distraction.DriverDistractionLevel;
import com.swedspot.vil.distraction.DriverDistractionListener;
import com.swedspot.vil.distraction.LightMode;
import com.swedspot.vil.distraction.StealthMode;
import com.swedspot.vil.policy.AutomotiveCertificate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class DriveActivity extends Activity {
    final static String authid = "5fe4551a599447929a301bc183b83a26";
    String stationName[];
    String airTemp[];
    String roadTemp[];
    String windSpd[];
    String stationCoords[];

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gps = new MyCurrentLocationListener(DriveActivity.this);
        setContentView(R.layout.driver_test);

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

        getWeather();
        for (int i = 0; i < averageBearing.length; i++){
            averageBearing[i] = gps.getBearing();
        }
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
        averageBearing();

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

    public void getWeather() {
        Parser p = new Parser(this);

        /*txtWindSpd = (TextView) findViewById(R.id.txtWindSpd);
        txtUpdateTime = (TextView) findViewById(R.id.txtUpdateTime);
        txtFirstBig = (TextView) findViewById(R.id.txtFirstBig);
        txtWindFrc = (TextView) findViewById(R.id.txtWindFrc);
        txtTempRoad = (TextView) findViewById(R.id.txtTempRoad);
        txtTempAir = (TextView) findViewById(R.id.txtTempAir);*/

        try {
            //TODO Try to make nicer! Perhaps create a method?
            JSONArray jsonArray = new JSONObject(p.execute().get(1000, TimeUnit.MILLISECONDS)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");
            stationName = new String[jsonArray.length()];
            airTemp = new String[jsonArray.length()];
            roadTemp = new String[jsonArray.length()];
            windSpd = new String[jsonArray.length()];
            stationCoords = new String[jsonArray.length()];

            Log.d("JSON", jsonArray.toString());



            for (int i = 0; i < stationName.length; i++) {
                stationName[i] = null;
                airTemp[i] = null;
                roadTemp[i] = null;
            }

            Log.d("JSON Station > ", "--------");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                String coordSplitter = station.getJSONObject("Geometry").getJSONObject("WGS84").getString("POINT");
                String coordArray[] = coordSplitter.split(" ");

                double statLon = Double.parseDouble(coordArray[0]);
                double statLat = Double.parseDouble(coordArray[1]);

                stationName[i] = station.getString("Name");
                airTemp[i] = station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                roadTemp[i] = station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                //stationCoords[i] = station.getString("Geometry.WGS84");
                //windSpd[i] = station.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                //Log.d("JSON Station >", station.getString("Name"));
                Log.d("JSON New", "------");
                Log.d("JSON Station > ", station.getString("Name"));
                Log.d("JSON Air Temp > ", station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp"));
                Log.d("JSON Road Temp > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
                Log.d("JSON Wind Force > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
                Log.d("JSON GPS > ", station.getJSONObject("Geometry").getJSONObject("WGS84").getString("POINT"));
            }
            Log.d("JSON Station > ", "--------");

            for (int i = 0; i <stationName.length; i++) {
                Random rand = new Random();
                DecimalFormat df = new DecimalFormat("#.00");
                double randomWind = 0 + (15 - 0) * rand.nextDouble();
                double airTempDouble = Double.parseDouble(airTemp[i]);
                double roadTempDouble = Double.parseDouble(roadTemp[i]);
                // Check for BS values

                txtStationName0.setText(stationName[0]);
                txtRoadtemp0.setText(roadTemp[0] + "°C");
                txtAirTemp0.setText(airTemp[0] + "°C");

                windSpd[i] = String.format("%.1f", randomWind);
            }

        } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public double getDistance(double statLat, double statLon) {
        double distanceInKm = 0;
        double gpsLat = gps.getLatitude(); //lat1
        double gpsLon = gps.getLongitude(); //lon1

        double dLat = Math.toRadians(statLat - gpsLat);
        double dLon = Math.toRadians(statLon - gpsLon);

        gpsLat = Math.toRadians(gpsLat);
        statLat = Math.toRadians(statLat);

        return distanceInKm;
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
        Log.d("Bearing - C", String.valueOf(gps.getLatitude()));
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
            for(int i = 0; i < averageBearing.length; i++){
                if (averageBearing[i] != 0){
                    averageCounter++;
                    calcAverage += averageBearing[i];
                }else {
                    i++;
                }
            }
            if (averageCounter > 5){
                averageCounter = 5;
            }

            avgBearing = (calcAverage / averageCounter)%360;
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


}






