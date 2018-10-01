package amastudio.ali.liveweather.alarmService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import amastudio.ali.liveweather.FavoriteCitiesActivity;
import amastudio.ali.liveweather.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ali on 19/08/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private final String CHANNEL_ID = "personal_notification";
    private final int NOTIFICATION_ID = 001;

    @Override
    public void onReceive(Context context, Intent intent) {

        //define new activity, if tapped on notification will open
        Intent liveWeatherActivity = new Intent(context, FavoriteCitiesActivity.class);
        liveWeatherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);    //set flag

        //define pending activity, later called on notification tapped
        PendingIntent liveweatherActivityPending = PendingIntent.getActivity(context, 0, liveWeatherActivity , PendingIntent.FLAG_ONE_SHOT);


        createNotificationChannel(context);     //create channel, for lower versions
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)        //set notification
                .setSmallIcon(R.drawable.liveweatherlogo_notification)
                .setContentTitle("Live Weather").setContentText("Good Morning,\n Leaving home or planning for today evening? Check weather first ;)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(liveweatherActivityPending);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());
        //Toast.makeText(context, "i'm running", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel(Context context) {   //define channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           String name = "Live Weather";
           String description = "Live weather notification";
           int importance = NotificationManager.IMPORTANCE_DEFAULT;
           NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
           notificationChannel.setDescription(description);
           NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
           notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
