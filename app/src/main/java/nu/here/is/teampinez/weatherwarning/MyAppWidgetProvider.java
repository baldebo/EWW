package nu.here.is.teampinez.weatherwarning;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;


public class MyAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Button getWindDirection;
        final Button getWindSpeed;
        Button getWindGust;
        final Button getRoadTemperature;
        final Button getStationName;



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

        SearchActivity searchActivity = new SearchActivity();

        for (int i = 0; i < N; i++){

            RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            v.setTextViewText(R.id.roadTemperatureBtn, (CharSequence) searchActivity.txtRdTemp);
            //v.setTextViewText(R.id.windDirectionBtn, (CharSequence) searchActivity.t);
            v.setTextViewText(R.id.windSpeedBtn, (CharSequence) searchActivity.txtWndSpd);
            //v.setTextViewText(R.id.windGustBtn, (CharSequence) searchActivity.txtRdTemp);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
          }
 }