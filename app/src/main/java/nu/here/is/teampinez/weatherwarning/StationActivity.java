package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    final static String authid = "5fe4551a599447929a301bc183b83a26";
    MyCurrentLocationListener gps;

    String stationName[];
    String airTemp[];
    String roadTemp[];
    String windSpd[];

    boolean firstRun = true;

    public static final String FIRST_COLUMN     ="First";
    public static final String SECOND_COLUMN    ="Second";
    public static final String THIRD_COLUMN     ="Third";
    public static final String FOURTH_COLUMN    ="Fourth";

    TextView txtWind;
    private ArrayList<HashMap<String,String>> list;

    public boolean moreThanOne = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_view);
        gps = new MyCurrentLocationListener(StationActivity.this);

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

            Log.d("JSON", jsonArray.toString());

            if (stationName.length > 1) {
                moreThanOne = true;
            } else {
                moreThanOne = false;
            }

            ListView listView = (ListView) findViewById(R.id.listStations);

            list = new ArrayList<HashMap<String,String>>();


            for (int i = 0; i < stationName.length; i++) {

                stationName[i] = null;
                airTemp[i] = null;
                roadTemp[i] = null;
            }

            Log.d("JSON Station > ", "--------");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                stationName[i] = station.getString("Name");
                airTemp[i] = station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                roadTemp[i] = station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                //windSpd[i] = station.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                //Log.d("JSON Station >", station.getString("Name"));
                Log.d("JSON New", "------");
                Log.d("JSON Station > ", station.getString("Name"));
                Log.d("JSON Air Temp > ", station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp"));
                Log.d("JSON Road Temp > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
                Log.d("JSON Wind Force > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
            }
            Log.d("JSON Station > ", "--------");

            for (int i = 0; i <stationName.length; i++) {
                Random rand = new Random();
                DecimalFormat df = new DecimalFormat("#.00");
                double randomWind = 0 + (15 - 0) * rand.nextDouble();
                double airTempDouble = Double.parseDouble(airTemp[i]);
                double roadTempDouble = Double.parseDouble(roadTemp[i]);
                // Check for BS values

                /*if (airTempDouble < -50) {
                    airTemp[i] = "N/A";
                } else {
                    airTemp[i] += "°C";
                }*/
                if (roadTempDouble < -50) {
                    roadTemp[i] = "N/A";
                } else {
                    roadTemp[i] += "°C";
                }

                windSpd[i] = String.format("%.1f", randomWind);
                //windSpd[i] += " m/s";

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
