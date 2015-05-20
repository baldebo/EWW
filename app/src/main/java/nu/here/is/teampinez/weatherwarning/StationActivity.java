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

    // Station Array.
    ArrayList<Station> stations = new ArrayList<>();
    ArrayList<HashMap<String, String>> list = new ArrayList<>();

    ListViewAdapter adapter;
    ListView listView;

    //TODO Remove, debugging only.
    MyCurrentLocationListener gps;

    public static final String FIRST_COLUMN     ="First";
    public static final String SECOND_COLUMN    ="Second";
    public static final String THIRD_COLUMN     ="Third";
    public static final String FOURTH_COLUMN    ="Fourth";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_view);

        /*
         * Setting up the ListView.
         * Adding adapter and setting header.
         */
        listView = (ListView) findViewById(R.id.listStations);
        adapter = new ListViewAdapter(this, list);
        listView.addHeaderView(View.inflate(this,R.layout.station_header, null));
        listView.setAdapter(adapter);

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
            for(int i=0; i<jsonArray.length(); i++) {
                Station s = new Station();
                JSONObject parsedStation = jsonArray.getJSONObject(i);

                // Parse data.
                s.name = parsedStation.getString("Name");

                if(parsedStation.getJSONObject("Measurement").getJSONObject("Air").length() == 0) {
                    s.airTemp = "N/A";
                } else {
                    s.airTemp = parsedStation.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                }
                if(parsedStation.getJSONObject("Measurement").getJSONObject("Road").length() == 0) {
                    s.roadTemp = "N/A";
                } else {
                    s.roadTemp = parsedStation.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                }
                if(parsedStation.getJSONObject("Measurement").getJSONObject("Wind").length() == 0) {
                    s.windSpeed = "N/A";
                } else {
                    s.windSpeed = parsedStation.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                }

                try {
                    if (!s.roadTemp.equals("N/A")) {
                        double roadTempDouble = Double.parseDouble(s.roadTemp);
                        if (roadTempDouble < -50) {
                            s.roadTemp = "N/A";
                        } else {
                            s.roadTemp += "°C";
                        }
                    }
                } catch (NumberFormatException e) {
                    Log.i(getClass().getName(), String.valueOf(e));

                }

                try {
                    if(!s.airTemp.equals("N/A")) {
                        double airTempDouble = Double.parseDouble(s.airTemp);
                        if(airTempDouble < -50) {
                            s.airTemp = "N/A";
                        } else {
                            s.airTemp += "°C";
                        }
                    }
                } catch (NumberFormatException e) {
                    Log.i(getClass().getName(), e.toString());
                }

                if(!s.windSpeed.equals("N/A")) s.windSpeed += " m/s";

                // Add object to ArrayList
                stations.add(s);
            }

            // Do fun stuff for each Object in the ArrayList
            for(Station s : stations) {
                HashMap<String, String> tmp = new HashMap<>();
                tmp.put(FIRST_COLUMN, s.name);
                tmp.put(SECOND_COLUMN, s.roadTemp);
                tmp.put(THIRD_COLUMN, s.airTemp);
                tmp.put(FOURTH_COLUMN, s.windSpeed);
                list.add(tmp);
            }
            //ListAdapter stationListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , stationName);
            //ListView listStations = (ListView) findViewById(R.id.listStations);
            //listStations.setAdapter(stationListAdapter);

        } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

final class Station {
    String name;
    String roadTemp;
    String airTemp;
    String windSpeed;
    // More data?
}