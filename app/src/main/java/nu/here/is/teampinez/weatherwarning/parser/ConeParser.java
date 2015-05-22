package nu.here.is.teampinez.weatherwarning.parser;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import nu.here.is.teampinez.weatherwarning.ListViewAdapter;

/**
 * Created by max on 5/22/15.
 */
public class ConeParser extends Parser {
    private ListView listView;
    private Activity activity;

    public ConeParser(Activity activity, ListView listView, ArrayList<SWEREF99Position> positions) {
        super(activity);
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
        return sb.toString();
    }
}
