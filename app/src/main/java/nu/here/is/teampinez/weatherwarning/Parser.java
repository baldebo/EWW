package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Parser extends AsyncTask<Integer, Void, ArrayList<Station>> {
    private final static String authid = "5fe4551a599447929a301bc183b83a26";

    private ArrayList<Station> stations = new ArrayList<>();

    private ProgressDialog progressDialog;
    private LocationHandler locationHandler;
    private ListView listview;
    private Activity activity;
    View driveView;

    Parser(Activity activity) {
        super();
        locationHandler = new LocationHandler(activity);
        progressDialog = new ProgressDialog(activity);
    }

    Parser(Activity activity, View driveView) {
        super();
        locationHandler = new LocationHandler(activity);
        progressDialog = new ProgressDialog(activity);
        this.driveView = driveView;
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
                s.wgs84 = parsedStation.getJSONObject("Geometry").getString("WGS84");

                if (parsedStation.getJSONObject("Measurement").getJSONObject("Air").length() != 0) {
                    s.airTemp = parsedStation.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                }
                if (parsedStation.getJSONObject("Measurement").getJSONObject("Road").length() != 0) {
                    s.roadTemp = parsedStation.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                }
                if (parsedStation.getJSONObject("Measurement").getJSONObject("Wind").length() != 0) {
                    s.windSpeed = parsedStation.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                }
//                if(parsedStation.getJSONObject("Measurement").getJSONObject("Wind").getString("ForceMax").length() != 0) {
//                    s.windGust = parsedStation.getJSONObject("Measurement").getJSONObject("Wind").getString("ForceMax");
//                }

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
            try {
                for (Station station : s) {
                    if (station.roadTemp == null) {
                        station.roadTemp = "N/A";
                    } else {
                        double temp = Double.parseDouble(station.roadTemp);
                        if(temp < -50) {
                            station.roadTemp = "N/A";
                        } else {
                            station.roadTemp += "°C";
                        }
                    }
                    if (station.airTemp == null) {
                        station.airTemp = "N/A";
                    } else {
                        double temp = Double.parseDouble(station.airTemp);
                        if(temp < -50) {
                            station.airTemp = "N/A";
                        } else {
                            station.airTemp += "°C";
                        }
                    }
                    if (station.windSpeed == null) {
                        station.windSpeed = "N/A";
                    } else {
                        station.windSpeed += " m/s";
                    }

                }
            } catch(NumberFormatException e) {
                e.printStackTrace();
            }
            ListViewAdapter adapter = new ListViewAdapter(activity, stations);
            listview.setAdapter(adapter);
        }

        if(driveView != null) {
            TextView txtStationName0 = (TextView) driveView.findViewById(R.id.stationName);
            TextView txtStationDistance0 = (TextView) driveView.findViewById(R.id.stationDistance);
            TextView txtAirTemp0 = (TextView) driveView.findViewById(R.id.airTemp);
            TextView txtRoadtemp0 = (TextView) driveView.findViewById(R.id.roadTemp);
            TextView txtWindSpd0 = (TextView) driveView.findViewById(R.id.windSpd);

            TextView txtStationName1 = (TextView) driveView.findViewById(R.id.stationName2);
            TextView txtStationDistance1 = (TextView) driveView.findViewById(R.id.stationDistance2);
            TextView txtAirTemp1 = (TextView) driveView.findViewById(R.id.airTemp2);
            TextView txtRoadtemp1 = (TextView) driveView.findViewById(R.id.roadTemp2);
            TextView txtWindSpd1 = (TextView) driveView.findViewById(R.id.windSpd2);

            TextView txtStationName2 = (TextView) driveView.findViewById(R.id.stationName3);
            TextView txtStationDistance2 = (TextView) driveView.findViewById(R.id.stationDistance3);
            TextView txtAirTemp2 = (TextView) driveView.findViewById(R.id.airTemp3);
            TextView txtRoadtemp2 = (TextView) driveView.findViewById(R.id.roadTemp3);
            TextView txtWindSpd2 = (TextView) driveView.findViewById(R.id.windSpd3);

            for (Station station : s){
                // s.statDist = getDistance(locationHandler.coordinates.location.getLatitude(), locationHandler.coordinates.location.getLongitude());
                double usableCoordinates[] = splitCoordinates(station.wgs84);
                station.statDist = (getDistance(usableCoordinates[1],usableCoordinates[0]));
                Log.d("Station - ", station.name + " - " + station.statDist);
            }

            sortStations(s);

            //Station 1
            txtStationName0.setText(s.get(findStationByDistance(0, s)).name);
            txtStationDistance0.setText(String.format("%.1f", stations.get(0).statDist) + " km");
            txtAirTemp0.setText(s.get(findStationByDistance(0, s)).airTemp);
            txtRoadtemp0.setText(s.get(findStationByDistance(0, s)).roadTemp);
            txtWindSpd0.setText(s.get(findStationByDistance(0, s)).windSpeed);

            //Station 2
            txtStationName1.setText(s.get(findStationByDistance(20, s)).name);
            txtStationDistance1.setText(String.format("%.1f", stations.get(findStationByDistance(20, s)).statDist) + " km");
            txtAirTemp1.setText(s.get(findStationByDistance(20, s)).airTemp + "°C");
            txtRoadtemp1.setText(s.get(findStationByDistance(20, s)).roadTemp + "°C");
            txtWindSpd1.setText(s.get(findStationByDistance(20, s)).windSpeed + " m/s");

            //Station 3
            txtStationName2.setText(s.get(findStationByDistance(40, s)).name);
            txtStationDistance2.setText(String.format("%.1f", stations.get(findStationByDistance(40, s)).statDist) + " km");
            txtAirTemp2.setText(s.get(findStationByDistance(40, s)).airTemp + "°C");
            txtRoadtemp2.setText(s.get(findStationByDistance(40, s)).roadTemp + "°C");
            txtWindSpd2.setText(s.get(findStationByDistance(40, s)).windSpeed + " m/s");
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

    private void sortStations(ArrayList<Station> s){
        for (int i = 1; i < s.size(); i++) {
            Collections.sort(s, new Comparator< Station >() {
                @Override
                public int compare(Station c1, Station c2) {
                    return Double.compare(c1.statDist, c2.statDist);
                }
            });
        }
    }

    private double[] splitCoordinates (String coordinates){
        double[] splitCoords = new double[2];

        coordinates = coordinates.replace("POINT", "");
        coordinates = coordinates.replace("(", "");
        coordinates= coordinates.replace(")", "");

        String[] parsedCoords = coordinates.split(" ");
        splitCoords[0] = Double.parseDouble(parsedCoords[1]);
        splitCoords[1] = Double.parseDouble(parsedCoords[2]);

        return splitCoords;
    }

    private int findStationByDistance(double distance, ArrayList<Station> s){

        double myDistance = Math.abs(s.get(0).statDist - distance);
        int iKeeper = 0;

        for (int i = 0; i < s.size(); i++){
            double iDistance = Math.abs(s.get(i).statDist - distance);
            if (iDistance < myDistance){
                iKeeper = i;
                myDistance = iDistance;
            }
        }
        return iKeeper;
    }

    private double getDistance(double statLat, double statLon) {
        Log.d("Notification", "Starting getDistance");
        double v = 6372.8;

        double gpsLat = locationHandler.coordinates.location.getLatitude(); //lat1
        double gpsLon = locationHandler.coordinates.location.getLongitude(); //lon1

        double dLat = Math.toRadians(statLat - gpsLat);
        double dLon = Math.toRadians(statLon - gpsLon);

        gpsLat = Math.toRadians(gpsLat);
        statLat = Math.toRadians(statLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(gpsLat) * Math.cos(statLat);
        double c = 2 * Math.asin(Math.sqrt(a));

        return v * c;
    }
}

final class Station {
    String name;
    String roadTemp;
    String airTemp;
    String windSpeed;
    String windGust;
    String wgs84;
    Double statDist;
    // More data?
}
