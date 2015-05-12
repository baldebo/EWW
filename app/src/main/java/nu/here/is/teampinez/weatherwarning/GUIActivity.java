package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
//  MAP IMPORTS //
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



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

    public void openHelp(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void openFavorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
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

    public void informationClicked(View view){


        final NotificationCompat.Builder notification = null;
         final int mId = 12345;
        TextView informationButton = (TextView) findViewById(R.id.textHeader);
         final int PRIORITY_MAX = 2;
        //build notification
        assert false;
        notification.setSmallIcon(R.drawable.notification_warning);
        notification.setTicker("This is the ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("This is the title");
        notification.setContentText("This is the body text");

        Intent intent = new Intent(this, SettingsActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notification.setContentIntent(pendingIntent);


                //issue notification
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(mId, notification.build());                }        });

    }


}
