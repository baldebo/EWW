package nu.here.is.teampinez.weatherwarning.parser;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import nu.here.is.teampinez.weatherwarning.ListViewAdapter;

/**
 * ConeParser
 */
public class ConeParser extends Parser {
    private ListView listView;
    private Activity activity;

    /**
     * ConeParser Construction method
     *
     * @param activity Activity to bind to.
     * @param listView ListView to output to.
     * @param positions Positions to calculate cone with.
     */
    public ConeParser(Activity activity, ListView listView, ArrayList<SWEREF99Position> positions) {
        super();
        this.listView = listView;
        this.activity = activity;

        try {
            this.execute(paramsCone(positions)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Station> s) {
        Log.v(getClass().getName(), "Executed ConeParser");
        if(listView != null) {
            ListViewAdapter adapter = new ListViewAdapter(activity, s);
            listView.setAdapter(adapter);
        }
    }

    /**
     * Building string to pass to Trafikverket API
     * http://api.trafikinfo.trafikverket.se/
     *
     * @param positions Positions to use for cone.
     * @return String formatted to API Standards.
     */
    private String paramsCone(ArrayList<SWEREF99Position> positions) {
        StringBuilder sb =  new StringBuilder();
        sb.append("<REQUEST>");
        sb.append("<LOGIN authenticationkey='").append(authid).append("' />");
        sb.append("<QUERY objecttype='WeatherStation'>");
        sb.append("<FILTER>");
        sb.append("<WITHIN name='Geometry.SWEREF99TM' shape='polygon' value='").append(positions.get(0).getLongitude()).append(" ").append(positions.get(0).getLatitude()).append(",").append(positions.get(1).getLongitude()).append(" ").append(positions.get(1).getLatitude()).append(",").append(positions.get(2).getLongitude()).append(" ").append(positions.get(2).getLatitude()).append(",").append(positions.get(0).getLongitude()).append(" ").append(positions.get(0).getLatitude()).append("' /></FILTER>");

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
