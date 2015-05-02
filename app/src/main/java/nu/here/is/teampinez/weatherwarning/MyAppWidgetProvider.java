package nu.here.is.teampinez.weatherwarning;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyAppWidgetProvider extends AppWidgetProvider {

    TextView windSpeed;
    TextView roadTemperature;
    TextView stationNameText;

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Toast.makeText(context, "Widget Deleted", Toast.LENGTH_SHORT).show();
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;


        TextView textRefresh = null;
        Button refresh = (Button) findViewById(R.id.refresh);


        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, GUIActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            views.setOnClickPendingIntent(R.id.stationName, pendingIntent);
            views.setOnClickPendingIntent(R.id.windDirectionBtn, pendingIntent);
            views.setOnClickPendingIntent(R.id.windSpeedBtn, pendingIntent);
            views.setOnClickPendingIntent(R.id.windGustBtn, pendingIntent);
            views.setOnClickPendingIntent(R.id.roadTemperatureBtn, pendingIntent);

            //views.setTextViewText(R.id.windSpeedBtn, (CharSequence) txtWndSpd);
            //views.setTextViewText(R.id.roadTemperatureBtn, (CharSequence) txtRdTemp);
            //views.setTextViewText(R.id.stationName, (CharSequence) txtStatName);



    public void onReceive(Context context, Intent intent){
            Parser p = new Parser(MyAppWidgetProvider.this);
            try {
                //TODO Try to make nicer!
                JSONArray jsonArray = new JSONObject(p.execute().get(1000, TimeUnit.MILLISECONDS)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");

                Log.d("JSON", jsonArray.toString());
                final String stationName[] = new String[jsonArray.length()];
                final String airTemp[] = new String[jsonArray.length()];
                final String roadTemp[] = new String[jsonArray.length()];
                final String windSpd[] = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject station = jsonArray.getJSONObject(i);
                    stationName[i] = station.getString("Name");
                    airTemp[i] = station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
                    roadTemp[i] = station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
                    windSpd[i] = station.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
                    //Log.d("JSON Station >", station.getString("Name"));

                    Log.d("JSON Station > ", station.getString("Name"));
                    Log.d("JSON Air Temp > ", station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp"));
                    Log.d("JSON Road Temp > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
                    Log.d("JSON Wind Force > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));

                }

                refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stationNameText.setText("Name: " + stationName[0]);
                        roadTemperature.setText("Road Temperature: " + roadTemp[0] + "°C");
                        windSpeed.setText("Wind Speed: " + windSpd[0] + " m/s");
                    }
                });
            } catch (JSONException | TimeoutException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            stationNameText = (TextView) RemoteViews(R.id.stationNameBtn, (CharSequence) stationNameText);
            roadTemperature = (TextView) findViewById(R.id.roadTemperatureBtn);
            windSpeed = (TextView) findViewById(R.id.windSpeedBtn);


            //            final SearchActivity searchActivity = new SearchActivity();

            //   for (int i = 0; i < N; i++){

            //       RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            //       v.setTextViewText(R.id.roadTemperatureBtn, (CharSequence) searchActivity.txtRdTemp);
            //       //v.setTextViewText(R.id.windDirectionBtn, (CharSequence) searchActivity.t);
            //       v.setTextViewText(R.id.windSpeedBtn, (CharSequence) searchActivity.txtWndSpd);
            //       //v.setTextViewText(R.id.windGustBtn, (CharSequence) searchActivity.txtRdTemp);
            //       appWidgetManager.updateAppWidget(appWidgetId, views);

            //   }
            //       Button refresh = (Button) findViewById(R.id.refresh);
            //       refresh.setOnClickListener(new View.OnClickListener() {
            //       @Override
            //       public void onClick(View v) {
            //           roadTemperature.setText("Road Temperature: " + searchActivity.roadTemp[0] + "°C");
            //           windSpeed.setText("Wind Speed: " + windSpd[0] + " m/s");
            //       }
            //   }

            //               // Tell the AppWidgetManager to perform an update on the current app widget
            //               appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        }

//    Button refresh = (Button) RemoteViews(R.id.refresh);
        TextView txtStatName;
        TextView txtAirTemp;
        TextView txtRdTemp;
        TextView txtWndSpd;
//    Parser p = new Parser(MyAppWidgetProvider.this);
//    try {
//        //TODO Try to make nicer!
//        JSONArray jsonArray = new JSONObject(p.execute().get(1000, TimeUnit.MILLISECONDS)).getJSONObject("RESPONSE").getJSONArray("RESULT").getJSONObject(0).getJSONArray("WeatherStation");
//
//        Log.d("JSON", jsonArray.toString());
//        final String stationName[] = new String[jsonArray.length()];
//        final String airTemp[] = new String[jsonArray.length()];
//        final String roadTemp[] = new String[jsonArray.length()];
//        final String windSpd[] = new String[jsonArray.length()];
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject station = jsonArray.getJSONObject(i);
//            stationName[i] = station.getString("Name");
//            airTemp[i] = station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp");
//            roadTemp[i] = station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp");
//            windSpd[i] = station.getJSONObject("Measurement").getJSONObject("Wind").getString("Force");
//            //Log.d("JSON Station >", station.getString("Name"));
//
//            Log.d("JSON Station > ", station.getString("Name"));
//            Log.d("JSON Air Temp > ", station.getJSONObject("Measurement").getJSONObject("Air").getString("Temp"));
//            Log.d("JSON Road Temp > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
//            Log.d("JSON Wind Force > ", station.getJSONObject("Measurement").getJSONObject("Road").getString("Temp"));
//
//        }
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                txtStatName.setText("Name: " + stationName[0]);
//                txtAirTemp.setText("Air Temperature: " + airTemp[0] + "°C");
//                txtRdTemp.setText("Road Temperature: " + roadTemp[0] + "°C");
//                txtWndSpd.setText("Wind Speed: " + windSpd[0] + " m/s");
//            }
//        });
//    } catch (JSONException| TimeoutException| ExecutionException| InterruptedException e) {
//        e.printStackTrace();


    }

    }

//    txtStatName = (TextView) findViewById(R.id.name);
//    txtAirTemp = (TextView) findViewById(R.id.temp_air);
//    txtRdTemp = (TextView) findViewById(R.id.temp_road);
//    txtWndSpd = (TextView) findViewById(R.id.wind);

