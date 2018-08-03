package com.userapp.notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.userapp.MainActivity;
import com.userapp.R;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private int Request_Code = 12321;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null) {
            Log.v("fcmConsoleNotification", "fcmConsoleNotification");
            fcmConsoleNotification(remoteMessage.getNotification());
        }


    }

//    private void sendNotificationAppClose(String click_action, String title, String body, String content) {
//
//
//        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
//        notificationLayout.setTextViewText(R.id.notification_title, title);
//        notificationLayout.setTextViewText(R.id.notification_body, body);
//        Bitmap vbdIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, getResources().getString(
//                R.string.default_notification_channel_id));
//        nb.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
//        nb.setCustomContentView(notificationLayout);
//        nb.setSmallIcon(R.mipmap.ic_launcher);
//        nb.setContentTitle(title);
//        nb.setChannelId(getResources().getString(
//                R.string.default_notification_channel_id));
////        nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        nb.setPriority(Notification.PRIORITY_HIGH);
//        nb.setContentText(body);
//
//        Intent resultIntent = new Intent(this, DashboardActivity.class);
//        resultIntent.putExtra("intent", click_action);
//        if (click_action.equalsIgnoreCase("admin")) {
//            resultIntent.putExtra("title", title);
//            resultIntent.putExtra("body", body);
//            resultIntent.putExtra("content", content);
//        }
//
//
//        TaskStackBuilder TSB = TaskStackBuilder.create(this);
//        TSB.addParentStack(DashboardActivity.class);
//        TSB.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent = TSB.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        nb.setContentIntent(resultPendingIntent);
//        // Adds the Intent that starts the Activity to the top of the stack
//        nb.setAutoCancel(true);
//        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        // mId allows you to update the notification later on.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id),
//                    getResources().getString(R.string.default_notification_channel_name),
//                    NotificationManager.IMPORTANCE_HIGH);
//            channel.setDescription("");
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//            NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
//            notificationManager1.notify(Request_Code, nb.build());
//        } else {
//            mNotificationManager.notify(Request_Code, nb.build());
//        }
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();
//    }

    private void fcmConsoleNotification(RemoteMessage.Notification notification) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.default_notification_channel_id));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        mBuilder.setColor(ContextCompat.getColor(this, android.R.color.white))
                .setLargeIcon(bitmap) // notification icon
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle() + "")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getBody()))
                .setContentText(notification.getBody()) // message for notification
                .setAutoCancel(true); // clear notification after click

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, Request_Code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(
                    getResources().getString(R.string.default_notification_channel_name),
                    "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(getResources().getString(
                    R.string.default_notification_channel_id));
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(Request_Code, mBuilder.build());
    }


//    private void handleDataMessage(RemoteMessage message) {
//
//        Log.v(TAG, "push json: " + message.toString());
//
//        try {
////            JSONObject data = json.getJSONObject("data");
////
////            String title = data.getString("title");
////            String message = data.getString("message");
////            boolean isBackground = data.getBoolean("is_background");
////            String imageUrl = data.getString("image");
////            String timestamp = data.getString("timestamp");
////            JSONObject payload = data.getJSONObject("payload");
//            sendNotification(message);
//
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Create and show a simple notification containing the received FCM message.
//     *
//     * @param messageBody FCM message body received.
//     */
//    private void sendNotification(RemoteMessage messageBody) {
//
//        Intent intent = new Intent(this, DashboardActivity.class);
//        intent.putExtra("notify", "notify");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, Request_Code, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
//                        .setContentTitle(messageBody.getNotification().getTitle() + "\n " + messageBody.getNotification().getBody())
//                        .setContentText(messageBody.getMessageType())
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//        notificationManager.notify(Request_Code, notificationBuilder.build());
//    }


}