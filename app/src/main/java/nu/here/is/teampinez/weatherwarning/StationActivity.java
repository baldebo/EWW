package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class StationActivity extends Activity {
    String stationName[];
    String airTemp[];
    String roadTemp[];
    String windSpd[];

    //TODO Remove, debugging only.
    MyCurrentLocationListener gps;

    boolean firstRun = true;

    public static final String FIRST_COLUMN     ="First";
    public static final String SECOND_COLUMN    ="Second";
    public static final String THIRD_COLUMN     ="Third";
    public static final String FOURTH_COLUMN    ="Fourth";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_view);

        //TODO Remove, debugging only.
        gps = new MyCurrentLocationListener(StationActivity.this);
        gps.getTriangle();

        getWeather();

        //TODO Remove button? Timed update?
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather();
            }
        });
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

        try {
            //TODO Try to make nicer! Perhaps create a method?
            JSONArray jsonArray = new JSONObject(p.execute().get(1000, TimeUnit.MILLISECONDS)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");
            stationName = new String[jsonArray.length()];
            airTemp = new String[jsonArray.length()];
            roadTemp = new String[jsonArray.length()];
            windSpd = new String[jsonArray.length()];

            ListView listView = (ListView) findViewById(R.id.listStations);
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < stationName.length; i++) {
                stationName[i] = null;
                airTemp[i] = null;
                roadTemp[i] = null;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                stationName[i] = station.getString("Name");
                if(station.getJSONObject("Measurement").getJSONObject("Air").length() == 0) {
                    airTemp[i] = "N/A";
                } else {
                    airTemp[i] = station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                }
                if(station.getJSONObject("Measurement").getJSONObject("Road").length() == 0) {
                    roadTemp[i] = "N/A";
                } else {
                    roadTemp[i] = station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                }
                if(station.getJSONObject("Measurement").getJSONObject("Wind").length() == 0) {
                    windSpd[i] = "N/A";
                } else {
                    windSpd[i] = station.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                }
            }

            for (int i = 0; i <stationName.length; i++) {
                Random rand = new Random();
                DecimalFormat df = new DecimalFormat("#.00");
                double randomWind = 0 + (15 - 0) * rand.nextDouble();
                try {
                    if (!roadTemp[i].equals("N/A")) {
                        double roadTempDouble = Double.parseDouble(roadTemp[i]);
                        if (roadTempDouble < -50) {
                            roadTemp[i] = "N/A";
                        } else {
                            roadTemp[i] += "°C";
                        }
                    }
                } catch (NumberFormatException e) {
                    Log.i(getClass().getName(), e+" "+String.valueOf(roadTemp[i]));
                }

                try {
                    if(!airTemp[i].equals("N/A")) {
                        double airTempDouble = Double.parseDouble(airTemp[i]);
                        if(airTempDouble < -50) {
                            airTemp[i] = "N/A";
                        } else {
                            airTemp[i] += "°C";
                        }
                    }
                } catch (NumberFormatException e) {
                    Log.i(getClass().getName(), e.toString());
                }

                if(!windSpd[i].equals("N/A")) windSpd[i] += " m/s";

                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put(FIRST_COLUMN, stationName[i]);
                temp.put(SECOND_COLUMN, roadTemp[i]);
                temp.put(THIRD_COLUMN, airTemp[i]);
                temp.put(FOURTH_COLUMN, windSpd[i]);
                list.add(temp);
            }

            if (firstRun){
                View view = View.inflate(this, R.layout.station_header, null);
                listView.addHeaderView(view);
                firstRun = false;
            }

            ListViewAdapter adapter = new ListViewAdapter(this, list);
            listView.setAdapter(adapter);

            //ListAdapter stationListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , stationName);
            //ListView listStations = (ListView) findViewById(R.id.listStations);
            //listStations.setAdapter(stationListAdapter);

        } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
