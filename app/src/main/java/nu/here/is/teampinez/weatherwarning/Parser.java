package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by max on 2015-04-24.
 */
public class Parser extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;

    Parser(Activity activity) {
        super();
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

    @Override
    protected String doInBackground(String... params) {
        return getJson(500);
    }

    @Override
    protected void onPostExecute(String v) {
        progressDialog.dismiss();
        Log.e("Post Execute", v);
    }

    private String getJson(int timeout) {
        HttpURLConnection c = null;
        try {
            URL url = new URL("http://api.trafikinfo.trafikverket.se/beta/data.json");
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setDoOutput(true);
            c.setDoInput(true);
            c.setRequestProperty("Content-Type", "text/xml");
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);

            OutputStream os = c.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(params(SearchActivity.authid, 320011, 6398983, 10000));

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

                    // Log.i("Response", sb.toString());

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

    private String params(String authid, int lat, int lon, int radius) {
        StringBuilder sb = new StringBuilder();
        sb.append("<REQUEST>");
        sb.append("<LOGIN authenticationkey='").append(authid).append("' />");
        sb.append("<QUERY objecttype='WeatherStation'>");
        sb.append("<FILTER>").append("<WITHIN name='Geometry.SWEREF99TM' shape='center' value='").append(lat).append(" ").append(lon).append("' radius='").append(radius).append("'").append("/></FILTER>");
        sb.append("<INCLUDE>Name</INCLUDE>");
        sb.append("</QUERY></REQUEST>");

        Log.i("Request", sb.toString());

        return sb.toString();
    }
}
