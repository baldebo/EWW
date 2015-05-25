package nu.here.is.teampinez.weatherwarning.parser;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import nu.here.is.teampinez.weatherwarning.ListViewAdapter;


/**
 * RadiusParser Class
 */
public class RadiusParser extends Parser {
    private ListView listView;
    private Activity activity;

    /**
     * Constuction method
     *
     * @param activity Activity to bind to.
     * @param listView ListView to output to.
     * @param radius Radius for search, in meters.
     * @param position Center of search point.
     */
    public RadiusParser(Activity activity, ListView listView, Integer radius, SWEREF99Position position) {
        super();
        this.listView = listView;
        this.activity = activity;
        try {
            this.execute(paramsRadius(radius, position)).get(15000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Station> s) {
        Log.e(getClass().getName(), "Executed RadiusParser");
        if(listView != null) {
            ListViewAdapter adapter = new ListViewAdapter(activity, s);
            listView.setAdapter(adapter);
        }
    }

    /**
     * String to pass to Trafikverket API
     * @param radius Radius in meters.
     * @param position Center of search
     * @return Strin formatted to API standards.
     */
    private String paramsRadius(Integer radius, SWEREF99Position position) {
        StringBuilder sb = new StringBuilder();
        sb.append("<REQUEST>");
        sb.append("<LOGIN authenticationkey='").append(authid).append("' />");
        sb.append("<QUERY objecttype='WeatherStation'>");

        /*
         * Values lat and long position will be swapped sometime soon in the API.
         */
        sb.append("<FILTER>").append("<WITHIN name='Geometry.SWEREF99TM' shape='center' value='").append(position.getLongitude()).append(" ").append(position.getLatitude()).append("' radius='").append(radius).append("'").append("/></FILTER>");

        /*
         * More datas?
         */
        sb.append("<INCLUDE>Measurement.MeasureTime</INCLUDE>");
        sb.append("<INCLUDE>Name</INCLUDE>");
        sb.append("<INCLUDE>Measurement.Air.Temp</INCLUDE>");
        sb.append("<INCLUDE>Measurement.Road.Temp</INCLUDE>");
        sb.append("<INCLUDE>Measurement.Wind.Force</INCLUDE>");
        sb.append("<INCLUDE>Geometry.WGS84</INCLUDE>");
        sb.append("<INCLUDE>Measurement.Wind.ForceMax</INCLUDE>");
        sb.append("<INCLUDE>Measurement.Wind.DirectionText</INCLUDE>");
        sb.append("<INCLUDE>ModifiedTime</INCLUDE>");
        sb.append("</QUERY></REQUEST>");

        //TODO Remove debug logging
        Log.d(getClass().getName(), sb.toString());

        return sb.toString();
    }
}
