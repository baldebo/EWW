package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;



public class GUIActivity extends Activity {

    TextView theTextView;
    MyCurrentLocationListener gps;

    double locLat;
    double locLon;
    double locLonTemp;
    double locLatTemp;
    int locLatInt;
    int locLonInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);
        gps = new MyCurrentLocationListener(GUIActivity.this);
        if(gps.canGetLocation()){
            updateCoordinates();
        }
        ImageButton ourButton = (ImageButton) findViewById(R.id.imgBtnInformation);
        ourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps.canGetLocation()) {
                    updateCoordinates();
                    convertCoordsToInt();
                    theTextView.setText("Your location is\nLat: " + locLatInt + "\nLong: " + locLonInt);
                }
            }
        });

        theTextView = (TextView) findViewById(R.id.textHeader);
    }


    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void updateCoordinates() {
        locLat = gps.getLatitude();
        locLon = gps.getLongitude();
    }

    public void convertCoordsToInt() {
        locLonTemp = locLon * 100000;
        locLatTemp = locLat * 100000;
        locLatInt = (int) locLatTemp;
        locLonInt = (int) locLonTemp;
    }


}
