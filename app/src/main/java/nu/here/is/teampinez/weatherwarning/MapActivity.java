package nu.here.is.teampinez.weatherwarning;

import android.graphics.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
       // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager Object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get lat of the current loc
        double latitude = myLocation.getLatitude();

        // Get lon of current loc

        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in google map

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the google map
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.0958, 17.7934113)).title("Grödinge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.56085, 17.5717087)).title("Ö Bålsta").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.40578, 17.86487)).title("Barkarby").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.470314, 18.4123154)).title("Gräsö").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.30246, 17.8540974)).title("Drottningholm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.4018822, 17.87936)).title("Lövstabruk").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.9343872, 17.92341)).title("Nynäshamn").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.8219261, 17.1495132)).title("Svärta").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.81708, 17.4883461)).title("Skärfälten").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.470314, 16.5365677)).title("Hål").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.9955673, 17.56708)).title("Drälinge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.24436, 16.1033955)).title("Nya Hjälmaresund").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.0550156, 17.19934)).title("Grinda").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.2412376, 16.6029758)).title("Slytan").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.14063, 18.3931313)).title("Dalarö").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.23975, 18.1206913)).title("Farsta").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.70304, 18.4886761)).title("Ledinge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.32913, 18.3143444)).title("Gustavsberg").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.9488564, 17.6558456)).title("Uppsala").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.4285355, 18.2233143)).title("Vaxholm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.4860077, 17.7393227)).title("Skärplinge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.7742653, 16.8950653)).title("Simtuna").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.9501457, 16.8524914)).title("Vallhalla").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.7475052, 18.0470314)).title("Åby").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.7192535, 18.9305038)).title("Rådmansö").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.6434669, 17.893177)).title("Arlanda").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.4837761, 17.7646618)).title("Kungsängen").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.081192, 17.595932)).title("Järna").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.3447, 18.47916)).title("Värmdö").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.19509, 17.8339825)).title("Dannemora").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.6558533, 18.340004)).title("Söderhall").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.053093, 17.4470825)).title("Mölnbo").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.99017, 18.8100052)).title("Väddö").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.3409, 18.0111923)).title("Essingeleden, Karlb.").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.38418, 18.0421753)).title("Stocksund").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.30112, 14.9486456)).title("Väderstad").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.602314, 17.4524574)).title("Överboda").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.2365074, 18.29809)).title("Uppskedika").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.176815, 17.8069363)).title("Tumba").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.2578278, 18.1669922)).title("Älta").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.1844864, 17.6425629 )).title("Södertälje").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.2124138, 17.7630558)).title("Salem").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.1939659, 17.4389782 )).title("Turinge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.56332, 18.2320576 )).title("Brottby").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.8807678, 18.1754684)).title("Glugga").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.303112, 18.0210114 )).title("Essingeleden, Västb.").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.1991425, 17.663208 )).title("Moraberg").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.3689728, 18.0194912 )).title("Solna").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.40763, 16.3699684 )).title("Gröndal").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.57504, 18.6438351 )).title("Vätterhaga").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.03736, 16.5582352 )).title("Lidaviken").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.6231041, 17.3819675 )).title("Ekolsundsbron").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.20954, 17.9178963 )).title("Tullinge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.6277466, 17.7455578 )).title("Sigtuna").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.0380554, 18.4993744 )).title("Sättra").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.2825127, 17.9247417 )).title("Bredäng").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.5853462, 16.9967022 )).title("Svinnegarn").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.6347847, 16.8585663 )).title("Lillån").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.0442429, 18.0028114 )).title("Landfjärden").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.48931, 18.16463)).title("Ullnasjön").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.42, 17.4102612 )).title("Mehedeby").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.9081345, 17.3299484 )).title("Brohagen").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.757, 16.8532143 )).title("Enstaberga").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.12993, 18.1428051  )).title("Västerhaninge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.8160439, 17.7548141 )).title("Sävjaån").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.47623, 17.9177628 )).title("Rotebro").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.0818253, 16.8347683 )).title("Sparreholm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.27791, 17.7595139 )).title("Ekerö").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.592, 17.4869976 )).title("Bålsta").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.2046967, 16.82281 )).title("Björndammen").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.00612, 17.5409489 )).title("Lillsjön").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.4957542, 18.2215958 )).title("Åkersberga").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.8301125, 16.753706 )).title("Nykyrka").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.3604164, 16.9209747 )).title("Härad").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.66156, 17.2643375 )).title("Litslena").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.0671425, 15.8681993 )).title("Hacksta").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.3244, 17.5112953 )).title("Tierp").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.8425522, 16.1978874 )).title("Västeråsen").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.05133, 18.0540047 )).title("Alunda").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.2419243, 17.1772118 )).title("Läggesta").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.76579, 17.780632 )).title("Dalbo").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.57538, 16.5128365 )).title("Östra Husby").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.3572845, 18.1045189 )).title("Lidingöbron").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.37861, 16.7868938 )).title("Eskilstuna Ö").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.0495071, 17.6224861 )).title("Björklinge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.74248, 16.4726028 )).title("Nya Korsbäcken").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.6775, 16.2836456 )).title("Kolmården").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.8778267, 17.284771 )).title("Åland").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.4485779, 17.01234 )).title("Björndalsviken").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.43381, 15.6191587 )).title("Linköping").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.8924141, 18.6328354 )).title("Sonö").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.6902733, 17.0487652 )).title("Oxelösund").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.76549, 15.5188437 )).title("Hällestad").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(57.9114876, 15.6926746 )).title("Vadstugan").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.7162971, 16.1645756 )).title("Åby 2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.14181, 16.5342541 )).title("Valdemarsvik").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(59.05243, 16.2217312 )).title("Strängstorp").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(60.21594, 17.4955177 )).title("Nya Månkarbo").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(58.5315323, 16.0303574 )).title("Lövstad").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        /// Here stopped
        mMap.addMarker(new MarkerOptions().position(new LatLng(55.558815, 13.0847826 )).title("Oxie").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(57.7997627, 12.0001383 )).title("Angered").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(57.25981, 13.4790363 )).title("Gislaved").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(57.2944221, 15.5674095 )).title("Virserum").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(55.6386452, 14.2298861 )).title("Rörum").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(55.45452, 13.0069847 )).title("Vellinge").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(57.80554, 16.50228 )).title("Västervik").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(55.7680435, 13.305479 )).title("Gårdstånga").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        mMap.addMarker(new MarkerOptions().position(new LatLng(55.92922, 13.1084242 )).title("Svalöv").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE
        )));
        // 2000

    }
}
