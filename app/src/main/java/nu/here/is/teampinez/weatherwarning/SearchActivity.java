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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SearchActivity extends Activity {
    final static String authid = "5fe4551a599447929a301bc183b83a26";

    TextView txtStatName1;
    TextView txtAirTemp1;
    TextView txtRdTemp1;
    TextView txtWndSpd1;
    TextView txtStatName2;
    TextView txtAirTemp2;
    TextView txtRdTemp2;
    TextView txtWndSpd2;

    public boolean moreThanOne = false;
    boolean firstPass = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button btnSearch = (Button) findViewById(R.id.search_button);
        Parser p = new Parser(SearchActivity.this);

        txtStatName1 = (TextView) findViewById(R.id.txtStation1);
        txtAirTemp1 = (TextView) findViewById(R.id.txtAirTemp1);
        txtRdTemp1 = (TextView) findViewById(R.id.txtRoadTemp1);
        txtWndSpd1 = (TextView) findViewById(R.id.txtWind1);

        txtStatName2 = (TextView) findViewById(R.id.txtStation2);
        txtAirTemp2 = (TextView) findViewById(R.id.txtAirTemp2);
        txtRdTemp2 = (TextView) findViewById(R.id.txtRoadTemp2);
        txtWndSpd2 = (TextView) findViewById(R.id.txtWind2);

        setTxtAlpha(0.0);

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
                //windSpd[i] = station.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                //Log.d("JSON Station >", station.getString("Name"));

                Log.d("JSON Station > ", station.getString("Name"));
                Log.d("JSON Air Temp > ", station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp"));
                Log.d("JSON Road Temp > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
                Log.d("JSON Wind Force > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));

                if (i > 0) {
                    moreThanOne = true;
                }
            }

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firstPass = false;
                    setTxtAlpha(1.0);

                    txtStatName1.setText("Name: " + stationName[0]);
                    txtAirTemp1.setText("Air Temperature: " + airTemp[0] + "°C");
                    txtRdTemp1.setText("Road Temperature: " + roadTemp[0] + "°C");

                    txtStatName2.setText("Name: " + stationName[1]);
                    txtAirTemp2.setText("Air Temperature: " + airTemp[1] + "°C");
                    txtRdTemp2.setText("Road Temperature: " + roadTemp[1] + "°C");
                    //txtWndSpd1.setText("Wind Speed: " + windSpd[1] + " m/s");

                }
            });
        } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

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

    public void setTxtAlpha(double visibility) {
        float alphaF = (float) visibility;
        if (txtStatName1.getText() != null) {
            txtStatName1.setAlpha(alphaF);
            txtAirTemp1.setAlpha(alphaF);
            txtRdTemp1.setAlpha(alphaF);
            txtWndSpd1.setAlpha(alphaF);
        }
        if (moreThanOne && !firstPass) {
            txtStatName2.setAlpha(alphaF);
            txtAirTemp2.setAlpha(alphaF);
            txtRdTemp2.setAlpha(alphaF);
            txtWndSpd2.setAlpha(alphaF);
            System.out.println("Setting " + txtStatName2.getText() + " + visibility to " + visibility);
        }
    }

}
