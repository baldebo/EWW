package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

public class WidgetUpdater extends Activity implements OnClickListener {

    EditText info;
    AppWidgetManager awm;
    Context context;
    int awID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_main);
        TextView text = (TextView)findViewById(R.id.stationNameBtn);
        text.setOnClickListener(this);
        context = WidgetUpdater.this;
        info = (EditText)findViewById(R.id.stationName);
        //Getting info about the widget that launched this Activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            awID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        awm = AppWidgetManager.getInstance(context);
    }

    @Override
    public void onClick(View v) {

        String e = info.getText().toString();

        RemoteViews views = RemoteViews(context.getPackageName(), R.layout.widget_main);
        views.setTextViewText(R.id.);


    }
}
