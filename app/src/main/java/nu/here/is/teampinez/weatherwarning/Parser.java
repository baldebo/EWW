package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Parser extends AsyncTask<Integer, Void, ArrayList<Station>> {
    private final static String authid = "5fe4551a599447929a301bc183b83a26";

    ArrayList<Station> stations = new ArrayList<>();

    private ProgressDialog progressDialog;
    private LocationHandler locationHandler;
    private ListView listview;
    private Activity activity;

    Parser(Activity activity) {
        super();
        locationHandler = new LocationHandler(activity);
        progressDialog = new ProgressDialog(activity);
    }

    Parser(Activity activity, ListView listView) {
        super();
        locationHandler = new LocationHandler(activity);
        progressDialog = new ProgressDialog(activity);
        this.listview = listView;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    /**
     * (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Object[])
     *
     * Set params[0] to 1 for cone search. 0 for radius search.
     * Set params[1] when using radius to set the size.
     */
    @Override
    protected ArrayList<Station> doInBackground(Integer... params) {
        try {
            JSONArray jsonArray;
            if(params[0] == 1) {
                jsonArray = new JSONObject(getJson(1, null, params[2], 5000)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");
            } else {
                jsonArray = new JSONObject(getJson(0, params[1], null, 5000)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");
            }
            for(int i=0; i<jsonArray.length(); i++) {
                Station s = new Station();
                JSONObject parsedStation = jsonArray.getJSONObject(i);

                // Parse data.
                s.name = parsedStation.getString("Name");

                if(parsedStation.getJSONObject("Measurement").getJSONObject("Air").length() == 0) {
                    s.airTemp = "N/A";
                } else {
                    s.airTemp = parsedStation.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                }
                if(parsedStation.getJSONObject("Measurement").getJSONObject("Road").length() == 0) {
                    s.roadTemp = "N/A";
                } else {
                    s.roadTemp = parsedStation.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                }
                if(parsedStation.getJSONObject("Measurement").getJSONObject("Wind").length() == 0) {
                    s.windSpeed = "N/A";
                } else {
                    s.windSpeed = parsedStation.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                }

                try {
                    if (!s.roadTemp.equals("N/A")) {
                        double roadTempDouble = Double.parseDouble(s.roadTemp);
                        if (roadTempDouble < -50) {
                            s.roadTemp = "N/A";
                        } else {
                            s.roadTemp += "°C";
                        }
                    }
                } catch (NumberFormatException e) {
                    Log.i(getClass().getName(), String.valueOf(e));

                }

                try {
                    if(!s.airTemp.equals("N/A")) {
                        double airTempDouble = Double.parseDouble(s.airTemp);
                        if(airTempDouble < -50) {
                            s.airTemp = "N/A";
                        } else {
                            s.airTemp += "°C";
                        }
                    }
                } catch (NumberFormatException e) {
                    Log.i(getClass().getName(), e.toString());
                }

                if(!s.windSpeed.equals("N/A")) s.windSpeed += " m/s";

                // Add object to ArrayList
                stations.add(s);
            }
        } catch (SocketTimeoutException |JSONException e) {
            Log.e(getClass().getName(), String.valueOf(e));
        }

        return stations;
    }

    @Override
    protected void onPostExecute(ArrayList<Station> s) {
        progressDialog.dismiss();
        //TODO Remove debug logging
        if(listview != null) {
            ListViewAdapter adapter = new ListViewAdapter(activity, stations);
            listview.setAdapter(adapter);
        }
    }

    /**
     * @param searchType For a cone search use 1. For radius search use 0.
     * @param timeout Timeout in milliseconds
     *
     * @throws SocketTimeoutException
     */
    private String getJson(Integer searchType, @Nullable Integer radius, @Nullable Integer bearing, int timeout) throws SocketTimeoutException {
        HttpURLConnection c = null;
        try {
            URL url = new URL("http://api.trafikinfo.trafikverket.se/v1/data.json");
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setDoOutput(true);
            c.setDoInput(true);
            c.setRequestProperty("Content-Type", "text/xml");
//            c.setConnectTimeout(timeout);
//            c.setReadTimeout(timeout);

            OutputStream os = c.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            SWEREF99Position position = locationHandler.coordinates.getLoc();

            if(searchType == 1) {
                writer.write(paramsCone(authid, locationHandler.coordinates.getTriangle(bearing)));
            } else {
                writer.write(paramsRadius(authid, position, radius));
            }

            writer.flush();
            writer.close();
            os.close();

            c.connect();

            int status = c.getResponseCode();

            switch (status) {
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    Log.d(getClass().getName(), sb.toString());
                    return sb.toString();
            }

        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        } finally {
            if(c != null) {
                try {
                    c.disconnect();
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return null;
    }

    private String paramsRadius(String authid, SWEREF99Position position, Integer radius) {
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
        Log.d("Request", sb.toString());

        return sb.toString();
    }

    private String paramsCone(String authid, ArrayList<SWEREF99Position> positions) {
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
        Log.d("Request", sb.toString());

        return sb.toString();
    }
}

final class Station {
    String name;
    String roadTemp;
    String airTemp;
    String windSpeed;
    // More data?
}
