package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;

public class StationActivity extends Activity {


    ListView listView;

    LocationHandler locationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_view);

        locationHandler = new LocationHandler(this);

        /*
         * Setting up the ListView.
         * Setting header.
         */
        listView = (ListView) findViewById(R.id.listStations);
        listView.addHeaderView(View.inflate(this, R.layout.station_header, null));


        //TODO Remove button? Timed update?
        Button radius = (Button) findViewById(R.id.radiusSearch);
        Button cone = (Button) findViewById(R.id.coneSearch);
        Button clear = (Button) findViewById(R.id.clearSearch);

        cone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(getClass().getName(), String.valueOf(locationHandler.bearing.activeBearing));
                //getWeather(1, null, locationHandler.bearing.activeBearing);
                try {
                    new Parser(StationActivity.this, listView).execute(1, null, locationHandler.bearing.activeBearing).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(getClass().getName(), String.valueOf(locationHandler.coordinates.location.getLongitude()));
                Log.e(getClass().getName(), String.valueOf(locationHandler.coordinates.location.getLatitude()));
                try {
                    new Parser(StationActivity.this, listView).execute(0, 10000, null).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                //getWeather(0, 10000, null);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setAdapter(null);
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
}

final class Station {
    String name;
    String roadTemp;
    String airTemp;
    String windSpeed;
    // More data?
}