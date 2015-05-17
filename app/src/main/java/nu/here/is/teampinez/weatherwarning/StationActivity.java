package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class StationActivity extends Activity {
    final static String authid = "5fe4551a599447929a301bc183b83a26";
    MyCurrentLocationListener gps;



    TextView txtStatName1;
    TextView txtAirTemp1;
    TextView txtRdTemp1;
    TextView txtWndSpd1;
    TextView txtStatName2;
    TextView txtAirTemp2;
    TextView txtRdTemp2;
    TextView txtWndSpd2;
    TextView txtTitle;

    String stationName[];
    String airTemp[];
    String roadTemp[];
    String windSpd[];



    public boolean moreThanOne = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_view);
        gps = new MyCurrentLocationListener(StationActivity.this);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);

        // ListAdapter Starts

        // setTxtAlpha(0.0);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather();
                if (stationName == null) {
                    txtTitle.setText("No Stations Found");
                }

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
            ListAdapter stationListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stationName);
            ListView listStations = (ListView) findViewById(R.id.listStations);
            listStations.setAdapter(stationListAdapter);

            //setTxtAlpha(1.0);

   /*         txtStatName1.setText("Name: " + stationName[0]);
            txtAirTemp1.setText("Air Temperature: " + airTemp[0] + "°C");
            txtRdTemp1.setText("Road Temperature: " + roadTemp[0] + "°C");
            //txtWndSpd1.setText("Wind Speed: " + windSpd[1] + " m/s");

            if (!moreThanOne) {
                txtTitle.setText(stationName.length + " station");
                txtStatName2.setText("");
                txtAirTemp2.setText("");
                txtRdTemp2.setText("");
                txtWndSpd2.setText("");
            } else if (moreThanOne) {
                txtTitle.setText(stationName.length + " stations");
                txtStatName2.setText("Name: " + stationName[1]);
                txtAirTemp2.setText("Air Temperature: " + airTemp[1] + "°C");
                txtRdTemp2.setText("Road Temperature: " + roadTemp[1] + "°C");
            }*/
        } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
