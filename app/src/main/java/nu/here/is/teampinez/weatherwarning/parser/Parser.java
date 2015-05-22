package nu.here.is.teampinez.weatherwarning.parser;

import android.app.Activity;
import android.os.AsyncTask;

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
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by max on 5/22/15.
 */
public class Parser extends AsyncTask<String, Void, ArrayList<Station>> {
    final static String authid = "5fe4551a599447929a301bc183b83a26";
    private Activity activity;

    Parser(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Station> doInBackground(String... params) {
        ArrayList<Station> stations = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        /**
         * Fetch data.
         */
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

            OutputStream os = c.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(params[0]);

            writer.flush();
            writer.close();
            os.close();

            c.connect();
            int status = c.getResponseCode();
            switch(status) {
                case 200:
                    BufferedReader  br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(c != null) {
                try {
                    c.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Parse data
         */
        try {
            JSONArray jsonArray = new JSONObject(sb.toString()).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");
            for(int i=0; i<jsonArray.length(); i++) {
                Station s = new Station();
                JSONObject parsedStation = jsonArray.getJSONObject(i);

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
                stations.add(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stations;
    }
}