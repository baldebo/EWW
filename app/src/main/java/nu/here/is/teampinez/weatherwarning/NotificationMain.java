package nu.here.is.teampinez.weatherwarning;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jamel on 2015-05-07.
 */
public class NotificationMain extends ActionBarActivity {

    NotificationCompat.Builder notification;
    public static final int mId = 12345;
    TextView informationButton = (TextView) findViewById(R.id.textHeader);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        final NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_warning)
                .setContentTitle("My notification")
                .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, SettingsActivity.class);

        // The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(SettingsActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        mBuilder.setContentIntent(resultPendingIntent);
        final NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
            }
        });

    }

    public void informationClicked(View view){
        //build notification
        notification.setSmallIcon(R.drawable.notification_warning);
        notification.setTicker("This is the ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("This is the title");
        notification.setContentText("This is the body text");

        Intent intent = new Intent(this, NotificationMain.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        notification.setContentIntent(pendingIntent);


                        //issue notification
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(mId, notification.build());                }        });

    }
                }
