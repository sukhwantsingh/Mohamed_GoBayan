package com.libyasolutions.libyamarketplace.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.ChatDetailActivity;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.object.Conversation;
import com.libyasolutions.libyamarketplace.object.MessageEvent;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by GL62 on 6/7/2017.
 */

public class MessageReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;
    public Notification notification;
    public String userID = "";
    private String CHANNEL_ID = "Change_ID_Chat";

    @Override
    public void onReceive(final Context context, final Intent intent) {


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Chat channel";
            String description = "Show notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setVibrationPattern(new long[0]);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        if (MySharedPreferences.getInstance(context).getUserInfo() != null) {
            userID = MySharedPreferences.getInstance(context).getUserInfo().getId();
        }
        if (intent != null && intent.getAction().equals(Constant.NEW_MESSAGE)) {
            Conversation conversation = (Conversation) intent.getSerializableExtra("converstation");
            sendHeadUpnotifi(conversation, context, userID);

            EventBus.getDefault().post(new MessageEvent("conversation"));

        } else if (intent != null && intent.getAction().equals(Constant.READ_MESSAGE)) {
//            Utils.setBagde(context, Constant.countMessage);
        }
    }

    public void sendHeadUpnotifi(Conversation conversation, Context context, String userID) {
        Intent notificationIntent = new Intent(context.getApplicationContext(), ChatDetailActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Bundle bundle = new Bundle();
        if (conversation.getReceiverId().equals(userID)) {
            bundle.putString("idAgent", conversation.getSenderId());
            bundle.putString("title", conversation.getSenderName());
            bundle.putString("image", conversation.getImageSender());
            bundle.putString("data", conversation.getLastMessage());
            bundle.putString(Constant.SHOP_ID,conversation.getShopId());
            bundle.putString(Constant.SHOP_NAME,conversation.getShopName());
            bundle.putString(Constant.COUNT_SHOP,conversation.getCountShop());
        } else {
            bundle.putString("idAgent", conversation.getReceiverId());
            bundle.putString("title", conversation.getSenderName());
            bundle.putString("image", conversation.getImageSender());
            bundle.putString(Constant.SHOP_ID,conversation.getShopId());
            bundle.putString(Constant.SHOP_NAME,conversation.getShopName());
            bundle.putString(Constant.COUNT_SHOP,conversation.getCountShop());
        }
        notificationIntent.putExtra("bundle", bundle);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT > 26) {
            notification = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getResources().getString(R.string.message_from) + conversation.getSenderName())
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .setContentText(conversation.getLastMessage())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setColor(Color.argb(255, 38, 70, 109))
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getResources().getString(R.string.message_from) + conversation.getSenderName())
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .setContentText(conversation.getLastMessage())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setColor(Color.argb(255, 38, 70, 109))
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification = new Notification.Builder(context)
                    .setContentTitle(context.getResources().getString(R.string.message_from) + conversation.getSenderName())
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .setContentText(conversation.getLastMessage())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[0])
                    .build();
        } else {
            notification = new Notification.Builder(context)
                    .setContentTitle(context.getResources().getString(R.string.message_from) + conversation.getSenderName())
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .setContentText(conversation.getLastMessage())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build();
        }
        notificationManager.notify(Integer.valueOf(conversation.getSenderId()), notification);
        Log.d("ID NOTIFI", "sendHeadUpnotifi: " + conversation.getSenderId());
    }
}

