package com.libyasolutions.libyamarketplace.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.ManageOrderActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.ConvertShopOwnerActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.MainTabActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.OrderHistoryDetailV2;
import com.libyasolutions.libyamarketplace.config.Constant;

import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    String default_notification_channel_id = "MultipleRestaurant";

    private static int NOTIFICATION_ID = 1;
    public static final String KEY_MSG = "message";
    public static final String OBJECT_ID = "objectId";
    public static final String KEY_STATUS = "status";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "data: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String actionPush = data.get("actionPush");
            if (actionPush.equals("CHANGE_ORDER_STATUS")) {
                Intent intent = new Intent("CHANGE_ORDER_STATUS");
                intent.putExtra("CHANGE_ORDER_STATUS", "CHANGE_ORDER_STATUS");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                sendNotification(data, OrderHistoryDetailV2.class);
            } else if (actionPush.equals("NEW_ORDER")) {
                sendNotification(data, ManageOrderActivity.class);
            } else if (actionPush.equals("APPROVE_SHOP_OWNER")) {
                Intent intent = new Intent("REQUEST_SHOP_OWNER_ACCEPT");
                intent.putExtra("APPROVE_SHOP_OWNER", "APPROVE_SHOP_OWNER");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                sendNotification(data, MainTabActivity.class);
            } else if (actionPush.equals("REJECT_SHOP_OWNER")) {
                sendNotification(data, ConvertShopOwnerActivity.class);
                Intent intent = new Intent("REQUEST_SHOP_OWNER_REJECT");
                intent.putExtra("REJECT_SHOP_OWNER", "REJECT_SHOP_OWNER");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else if (actionPush.equals("ORDER_SUCCESS")) {
                sendNotification(data, OrderHistoryDetailV2.class);
            }
        } else if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "message: " + remoteMessage.getNotification().getBody());
            showNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(Map<String, String> data, Class<?> cla) {

        String channelId = default_notification_channel_id;
        ShortcutBadger.applyCount(this, ++Constant.countMessage);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        NOTIFICATION_ID = NOTIFICATION_ID + 1;
        String msg = data.get(KEY_MSG);
        String objectId = data.get(OBJECT_ID);
        String status = data.get(KEY_STATUS);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, cla);
        intent.putExtra("EXTRA_GROUP_ID", objectId);
        intent.putExtra("EXTRA_MESSAGE", msg);
        intent.putExtra("EXTRA_STATUS", status);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setContentTitle(this.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg).setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel multipleRestaurant notification",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void showNotification(String message) {
        Intent intent = new Intent(this, MainTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId ="Project_id"; // getString(R.string.project_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel Multiple Restaurant",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
