package nu.here.is.teampinez.weatherwarning.parser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import nu.here.is.teampinez.weatherwarning.AGAValues;
import nu.here.is.teampinez.weatherwarning.DriveActivity;
import nu.here.is.teampinez.weatherwarning.ListViewAdapter;
import nu.here.is.teampinez.weatherwarning.R;

/**
 * ConeParser
 */
public class ConeParser extends Parser {
    private ListView listView;
    private Activity activity;
    private View view;
    private Location location;
    private Context context;
    private int count1 = 0;
    private int count2 = 0;
    private boolean muteAlarm;

    /**
     * ConeParser Construction method
     * Used in StationActivity
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

    public ConeParser(Activity activity, View view, ArrayList<SWEREF99Position> positions, Location location) {
        super();
        this.activity = activity;
        this.view = view;
        this.location = location;

        try {
            this.execute(paramsCone(positions)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onPostExecute(ArrayList<Station> stations) {
        Log.v(getClass().getName(), "Executed ConeParser");
        if(listView != null) {
            ListViewAdapter adapter = new ListViewAdapter(activity, stations);
            listView.setAdapter(adapter);
        }

        DriveActivity mute = new DriveActivity();


        /* TODO Make this so much more nice :( */
        if(view != null) {
            for(Station s : stations) {
                double usableCoordinates[] = splitCoordinates(s.wgs84);
                s.statDist = (getDistance(usableCoordinates[1], usableCoordinates[0]));
            }
            sortStations(stations);
            for(Station s : stations) Log.d("Station - ", s.name + " - " + s.statDist);

            /* Vibrator to use for warning */
            DriveActivity act = DriveActivity.getInstance();
            Vibrator v = (Vibrator) act.getSystemService(Context.VIBRATOR_SERVICE);
            /* Playing the sound warning */
            final MediaPlayer mp = MediaPlayer.create(act, R.raw.warning);

            /* First stations is always the closest one */
            ((TextView) view.findViewById(R.id.stationName)).setText(stations.get(0).name);
            ((TextView) view.findViewById(R.id.stationDistance)).setText(String.format("%.1f", stations.get(0).statDist) + "km");

            count2 = count1;
            count1 = 0;
            Float airTemperino;
            Float roadTemperino;
            Float windSpeederino;

            if (stations.get(0).airTemp != null) {
                airTemperino = Float.parseFloat(stations.get(0).airTemp);}
            else airTemperino = 99f;
            if (stations.get(0).roadTemp != null) {
                roadTemperino = Float.parseFloat(stations.get(0).roadTemp);}
            else roadTemperino = 99f;
            if (stations.get(0).windSpeed != null) {
                windSpeederino = Float.parseFloat(stations.get(0).windSpeed);}
            else windSpeederino = 99f;

            /* Air Temperature with warnings */
            if(airTemperino > -90 && airTemperino < 3 && stations.get(0).airTemp != null ) {
                ((TextView) view.findViewById(R.id.airTemp)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.airTemp)).setText(stations.get(0).airTemp);
                count1++;
            } else if(stations.get(0).airTemp != null && airTemperino > -90) {
                ((TextView) view.findViewById(R.id.airTemp)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.airTemp)).setText(stations.get(0).airTemp);
            } else {
                ((TextView) view.findViewById(R.id.airTemp)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.airTemp)).setText("N/A");
            }
            /* Road Temperature with warnings */
            if (roadTemperino > -90 && roadTemperino < 3 && stations.get(0).roadTemp != null) {
                ((TextView) view.findViewById(R.id.roadTemp)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.roadTemp)).setText(stations.get(0).roadTemp);
                count1++;

            } else if (stations.get(0).windSpeed != null && roadTemperino > -90) {
                ((TextView) view.findViewById(R.id.roadTemp)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.roadTemp)).setText(stations.get(0).roadTemp);
            } else {
                ((TextView) view.findViewById(R.id.roadTemp)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.roadTemp)).setText("N/A");
            }

            /* Wind speed with warnings */
            if(windSpeederino > -90 && windSpeederino < 20 && stations.get(0).windSpeed != null ) {
                ((TextView) view.findViewById(R.id.windSpd)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.windSpd)).setText(stations.get(0).windSpeed);
                count1++;
            } else if (stations.get(0).windSpeed != null && windSpeederino > -90) {
                ((TextView) view.findViewById(R.id.windSpd)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.windSpd)).setText(stations.get(0).windSpeed);
            } else {
                ((TextView) view.findViewById(R.id.windSpd)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.windSpd)).setText("N/A");
            }




            /* Second station with warnings */
            ((TextView) view.findViewById(R.id.stationName2)).setText(stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).name);
            ((TextView) view.findViewById(R.id.stationDistance2)).setText(String.format("%.1f", stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).statDist) + "km");
            Float airTempF;
            Float roadTempF;
            Float windSpdF;

            if(stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).airTemp != null) {
                airTempF = Float.parseFloat(stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).airTemp);
            } else airTempF = 99f;
            if(stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).roadTemp != null) {
                roadTempF = Float.parseFloat(stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).roadTemp);
            } else roadTempF = 99f;
            if (stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).windSpeed != null) {
                windSpdF = Float.parseFloat(stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).windSpeed);
            } else windSpdF = 99f;

            if(airTempF > 1 && airTempF != null && airTempF > -90) {
                ((TextView) view.findViewById(R.id.airTemp2)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.airTemp2)).setText(Float.toString(airTempF) + "°C");
                count1++;
            } else if (airTempF < 90 && airTempF > -90) {
                ((TextView) view.findViewById(R.id.airTemp2)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.airTemp2)).setText(Float.toString(airTempF) + "°C");
            } else {
                ((TextView) view.findViewById(R.id.airTemp2)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.airTemp2)).setText("N/A°C");
            }
            if(roadTempF < 1 && airTempF != null && roadTempF > -90) {
                ((TextView) view.findViewById(R.id.roadTemp2)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.roadTemp2)).setText(Float.toString(roadTempF) + "°C");
                count1++;
            } else if (roadTempF < 90 && roadTempF > -90) {
                ((TextView) view.findViewById(R.id.roadTemp2)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.roadTemp2)).setText(Float.toString(roadTempF) + "°C");
            } else {
                ((TextView) view.findViewById(R.id.roadTemp2)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.roadTemp2)).setText("NA°C");
            }
            if(windSpdF > 15 && windSpdF != null && windSpdF > -90) {
                ((TextView) view.findViewById(R.id.windSpd2)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.windSpd2)).setText(Float.toString(windSpdF) + "m/s");
                count1++;
            } else if(windSpdF < 90 && windSpdF > -90) {
                ((TextView) view.findViewById(R.id.windSpd2)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.windSpd2)).setText(Float.toString(windSpdF) + "m/s");
            } else {
                ((TextView) view.findViewById(R.id.windSpd2)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.windSpd2)).setText("N/A m/s");
            }
            //((TextView) view.findViewById(R.id.airTemp2)).setText(stations.get(findStationByDistance((AGAValues.SPEED / 60) * 30, stations)).airTemp + "°C");
            //((TextView) view.findViewById(R.id.roadTemp2)).setText(stations.get(findStationByDistance((AGAValues.SPEED/60)*30, stations)).roadTemp + "°C");
            //((TextView) view.findViewById(R.id.windSpd2)).setText(stations.get(findStationByDistance((AGAValues.SPEED/60)*30, stations)).windSpeed + "m/s");

            /* Third Station with warnings */
            ((TextView) view.findViewById(R.id.stationName3)).setText(stations.get(findStationByDistance(AGAValues.SPEED, stations)).name);
            ((TextView) view.findViewById(R.id.stationDistance3)).setText(String.format("%.1f", stations.get(findStationByDistance(AGAValues.SPEED, stations)).statDist) + "km");

            Float airTempF2;
            Float roadTempF2;
            Float windSpdF2;

            if(stations.get(findStationByDistance(AGAValues.SPEED, stations)).airTemp != null) {
                airTempF2 = Float.parseFloat(stations.get(findStationByDistance(AGAValues.SPEED, stations)).airTemp);
            } else airTempF2 = 99f;
            if(stations.get(findStationByDistance(AGAValues.SPEED, stations)).roadTemp != null) {
                roadTempF2 = Float.parseFloat(stations.get(findStationByDistance(AGAValues.SPEED, stations)).roadTemp);
            } else roadTempF2 = 99f;

            if(stations.get(findStationByDistance(AGAValues.SPEED, stations)).windSpeed != null) {
                windSpdF2 = Float.parseFloat(stations.get(findStationByDistance(AGAValues.SPEED, stations)).windSpeed);
            } else windSpdF2 = 99f;
            if(airTempF2 < 3 && airTempF2 != null && airTempF2 > -90) {
                ((TextView) view.findViewById(R.id.airTemp3)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.airTemp3)).setText(Float.toString(airTempF2) + "°C");
                count1++;
            } else if(airTempF2 < 90 && airTempF2 > -90) {
                ((TextView) view.findViewById(R.id.airTemp3)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.airTemp3)).setText(Float.toString(airTempF2) + "°C");
            } else {
                ((TextView) view.findViewById(R.id.airTemp3)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.airTemp3)).setText("N/A°C");
            }
            if(roadTempF2 < 3 && roadTempF2 != null && roadTempF2 > -90) {
                ((TextView) view.findViewById(R.id.roadTemp3)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.roadTemp3)).setText(Float.toString(roadTempF2) + "°C");
                count1++;
            } else if(roadTempF2 < 90 && roadTempF2 > -90) {
                ((TextView) view.findViewById(R.id.roadTemp3)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.roadTemp3)).setText(Float.toString(roadTempF2) + "°C");
            } else {
                ((TextView) view.findViewById(R.id.roadTemp3)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.roadTemp3)).setText("N/A°C");
            }
            if(windSpdF2 > 15 && windSpdF2 != null && windSpdF2 > -90) {
                ((TextView) view.findViewById(R.id.windSpd3)).setTextColor(Color.RED);
                ((TextView) view.findViewById(R.id.windSpd3)).setText(Float.toString(windSpdF2) + "m/s");
                count1++;
            } else if (windSpdF2 < 90 && windSpdF2 > -90) {
                ((TextView) view.findViewById(R.id.windSpd3)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.windSpd3)).setText(Float.toString(windSpdF2) + "m/s");
            } else {
                ((TextView) view.findViewById(R.id.windSpd3)).setTextColor(Color.WHITE);
                ((TextView) view.findViewById(R.id.windSpd3)).setText("N/A m/s");
            }



            if (!act.getMuteAlert()){
                mp.start();
                v.vibrate(1000);
            }





            //((TextView) view.findViewById(R.id.airTemp3)).setText(stations.get(findStationByDistance(AGAValues.SPEED, stations)).airTemp + "°C");
            //((TextView) view.findViewById(R.id.roadTemp3)).setText(stations.get(findStationByDistance(AGAValues.SPEED, stations)).roadTemp + "°C");
            //((TextView) view.findViewById(R.id.windSpd3)).setText(stations.get(findStationByDistance(AGAValues.SPEED, stations)).windSpeed + "m/s");

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

    private double[] splitCoordinates(String coordinates) {
        double[] coords = new double[2];

        coordinates = coordinates.replace("POINT", "").replace("(", "").replace(")", "");

        String[] parsedCoords = coordinates.split(" ");
        coords[0] = Double.parseDouble(parsedCoords[1]);
        coords[1] = Double.parseDouble(parsedCoords[2]);

        return coords;
    }

    private int findStationByDistance(Float distance, ArrayList<Station> station) {
        double myDistance = Math.abs(station.get(0).statDist - distance);
        int iKeeper = 0;
        for(int i=0; i<station.size(); i++) {
            double iDistance = Math.abs(station.get(i).statDist - distance);
            if (iDistance < myDistance){
                iKeeper = i;
                myDistance = iDistance;
            }
        }
        return iKeeper;
    }

    private double getDistance(double statLat, double statLon) {
        double v = 6372.8;

        double gpsLat = location.getLatitude(); //lat1
        double gpsLon = location.getLongitude(); //lon1

        double dLat = Math.toRadians(statLat - gpsLat);
        double dLon = Math.toRadians(statLon - gpsLon);

        gpsLat = Math.toRadians(gpsLat);
        statLat = Math.toRadians(statLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(gpsLat) * Math.cos(statLat);
        double c = 2 * Math.asin(Math.sqrt(a));

        return v * c;
    }

    private void sortStations(ArrayList<Station> stations) {
        for(int i=1; i<stations.size(); i++) {
            Collections.sort(stations, new Comparator<Station>() {
                @Override
                public int compare(Station t1, Station t2) {
                    return Double.compare(t1.statDist, t2.statDist);
                }
            });
        }
    }
}
