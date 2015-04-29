package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SearchActivity extends Activity {
    final static String authid = "5fe4551a599447929a301bc183b83a26";

    TextView txtStatName;
    TextView txtAirTemp;
    TextView txtRdTemp;
    TextView txtWndSpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button btnSearch = (Button) findViewById(R.id.search_button);
        Parser p = new Parser(SearchActivity.this);
        try {
            //TODO Try to make nicer!
            JSONArray jsonArray = new JSONObject(p.execute().get(1000, TimeUnit.MILLISECONDS)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");

            Log.d("JSON", jsonArray.toString());
            final String stationName[] = new String[jsonArray.length()];
            final String airTemp[] = new String[jsonArray.length()];
            final String roadTemp[] = new String[jsonArray.length()];
            final String windSpd[] = new String[jsonArray.length()];
            boolean windError = false;

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
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtStatName.setText("Name: " + stationName[0]);
                    txtAirTemp.setText("Air Temperature: " + airTemp[0] + "°C");
                    txtRdTemp.setText("Road Temperature: " + roadTemp[0] + "°C");
                    txtWndSpd.setText("Wind Speed: " + windSpd[0] + " m/s");
                }
            });
        } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        txtStatName = (TextView) findViewById(R.id.name);
        txtAirTemp = (TextView) findViewById(R.id.temp_air);
        txtRdTemp = (TextView) findViewById(R.id.temp_road);
        txtWndSpd = (TextView) findViewById(R.id.wind);
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
}
