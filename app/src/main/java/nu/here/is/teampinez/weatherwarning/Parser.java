package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;

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


public class Parser extends AsyncTask<Integer, Void, String> {
    private final static String authid = "5fe4551a599447929a301bc183b83a26";

    private ProgressDialog progressDialog;
    MyCurrentLocationListener gps;

    Parser(Activity activity) {
        super();
        gps = new MyCurrentLocationListener(activity);
        progressDialog = new ProgressDialog(activity);
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
    protected String doInBackground(Integer... params) {
        try {
            if(params[0] == 1) {
                Log.d(getClass().getName(), String.valueOf(params[2]));
                return getJson(1, null, params[2], 5000);
            } else {
                return getJson(0, params[1], null, 5000);
            }
        } catch (SocketTimeoutException e) {
            Log.e(getClass().getName(), String.valueOf(e));
        }
        return null;
    }

    @Override
    protected void onPostExecute(String v) {
        progressDialog.dismiss();
        //TODO Remove debug logging
        Log.d("Post Execute", v);
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

            SWEREF99Position position = gps.getLoc();

            if(searchType == 1) {
                writer.write(paramsCone(authid, gps.getTriangle(bearing)));
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
