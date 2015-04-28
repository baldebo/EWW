package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;


public class GUIActivity extends Activity {

    TextView theTextView;
    MyCurrentLocationListener gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);

        ImageButton ourButton = (ImageButton) findViewById(R.id.imageButton5);
        ourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new MyCurrentLocationListener(GUIActivity.this);
                if (gps.canGetLocation()) {
                    double locLat = gps.getLatitude();
                    double locLon = gps.getLongitude();
                    double locLatTemp = locLat * 100000;
                    double locLonTemp = locLon * 100000;
                    int locLatInt = (int) locLatTemp;
                    int locLonInt = (int) locLonTemp;
                    DecimalFormat df = new DecimalFormat("#");

                    theTextView.setText("Your location is\nLat: " + locLatInt + "\nLong: " + locLonInt);
                }
            }
        });

        theTextView = (TextView) findViewById(R.id.textView13);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
