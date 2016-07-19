package com.indiainclusionsummit.indiainclusionsummit;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by I064893 on 9/18/2015.
 */
public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1000;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super(GCMIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // for some reason, this is getting executed everytime the app is started.
        // validate with message and then decide to call the sendNotification method for now as workaround.
        Bundle extras = intent.getExtras();
        Log.v("Avis", "Step 2 : On Handle Intent called.");
        if (!extras.isEmpty()) {

            // read extras as sent from server
            String message = extras.getString("message");
            String serverTime = extras.getString("timestamp");
            String paramCode  = extras.getString("actCode");
            if (serverTime != null ) {
                sendNotification("Message: " + message + "\n" + "Server Time: "
                        + serverTime , paramCode);

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg , String paramCode) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DetailsActivity.class), 0);

        switch(paramCode){
            case "1" :   // About Us
                contentIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, AboutUsActivity.class), 0);
                break;

            case "2" : // Feedback
                contentIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, FeedbackActivity.class), 0);
                break;

            case "3" : // TextCommentary
                 contentIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, TextCommentary.class), 0);
                break;

            default:
                // already added as DetailsActivity above the switch statement.
                break;
        }


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.iis_small_logo)
                .setContentTitle("Notification from IIS")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.v("Avis","Send Notification method in GCMIntent Service.");
    }

}
