package com.libyasolutions.libyamarketplace.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.PreferencesManager;
import com.libyasolutions.libyamarketplace.object.Conversation;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;



/**
 * Created by GL62 on 6/7/2017.
 */

public class MessageService extends Service {

    MessageReceiver receiver;
    private static String TAG = "Service";
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    public String lastTime = "";
    public String lastMessage = "";


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        receiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.NEW_MESSAGE);
        intentFilter.addAction(Constant.READ_MESSAGE);
        registerReceiver(receiver, intentFilter);

        final Intent i = new Intent();
        if (MySharedPreferences.getInstance(getBaseContext()).getUserInfo() != null) {
            final String userID = MySharedPreferences.getInstance(getBaseContext()).getUserInfo().getId();
            ref.child("user").child(userID).child("chatWith").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            ref.child("user").child(userID).child("chatWith").child(datas.getKey()).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
                                    lastTime = PreferencesManager.getInstance(getBaseContext()).getStringValue("lastTime");
                                    if (conversation != null) {
                                        if (!"0".equals(conversation.getUnread()) && !lastTime.equals(conversation.getDatePost())) {
                                            PreferencesManager.getInstance(getBaseContext()).putStringValue("lastTime", conversation.getDatePost());
                                            i.setAction(Constant.NEW_MESSAGE);
                                            i.putExtra("converstation", conversation);
                                            sendBroadcast(i);
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
                                    lastTime = PreferencesManager.getInstance(getBaseContext()).getStringValue("lastTime");
                                    lastMessage = PreferencesManager.getInstance(getBaseContext()).getStringValue("lastmessage");
                                    if (conversation != null) {
                                        if (!"0".equals(conversation.getUnread()) && !lastMessage.equals(conversation.getLastMessage())) {
                                            if (!"1".equals(conversation.getStatus())) {
                                                PreferencesManager.getInstance(getBaseContext()).putStringValue("lastTime", conversation.getDatePost());
                                                PreferencesManager.getInstance(getBaseContext()).putStringValue("lastmessage", conversation.getLastMessage());
                                                i.setAction(Constant.NEW_MESSAGE);
                                                i.putExtra("converstation", conversation);
                                                sendBroadcast(i);
                                            }
                                        } else if (!"0".equals(conversation.getUnread()) && !lastTime.equals(conversation.getDatePost())) {
                                            if (!"1".equals(conversation.getStatus())) {
                                                PreferencesManager.getInstance(getBaseContext()).putStringValue("lastTime", conversation.getDatePost());
                                                PreferencesManager.getInstance(getBaseContext()).putStringValue("lastmessage", conversation.getLastMessage());
                                                i.setAction(Constant.NEW_MESSAGE);
                                                i.putExtra("converstation", conversation);
                                                sendBroadcast(i);
                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
