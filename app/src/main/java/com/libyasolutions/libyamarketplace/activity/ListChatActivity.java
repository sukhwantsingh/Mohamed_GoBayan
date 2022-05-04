package com.libyasolutions.libyamarketplace.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ListChatAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.object.Conversation;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ListChatActivity extends BaseActivity {

    private TextView lblTitle;
    private ImageView btnBack;
    private ListView lsvAgent;
    private DatabaseReference ref;
    private ArrayList<Conversation> listConversation;
    private TextView tvNoData;
    private ListChatAdapter adapter;
    private Account userObj;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);
        ref = FirebaseDatabase.getInstance().getReference();
        initUI();
        initData();

    }


    public void initUI() {
        lsvAgent = findViewById(R.id.lsvAgent);
        btnBack = findViewById(R.id.iv_back);
        tvNoData = findViewById(R.id.tvNoData);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void initData() {
        userObj = MySharedPreferences.getInstance(self).getUserInfo();

        if (userObj != null) {
            //Get ListConversation.
            ref.child("user").child(userObj.getId()).child("chatWith").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listConversation = new ArrayList<Conversation>();
//                    keyConversation = new ArrayList<String>();
                    for (final DataSnapshot child : dataSnapshot.getChildren()) {
                        for (DataSnapshot chidData : child.getChildren()) {
                            final Map<String, Object> listData = (Map<String, Object>) chidData.getValue();
                            final Conversation conversation = new Conversation();

                            Object status = listData.get("status");
                            if (status != null && listData.get("datePost") != null) {
                                Object datePost = listData.get("datePost");
                                conversation.setDatePost(datePost.toString());
                                conversation.setLastMessage(listData.get("lastMessage") + "");
                                conversation.setImageReceiver(listData.get("imageReceiver") + "");
                                conversation.setImageSender(listData.get("imageSender") + "");
                                conversation.setReceiverName(listData.get("receiverName") + "");
                                conversation.setSenderName(listData.get("senderName") + "");
                                conversation.setSenderId(listData.get("senderId") + "");
                                conversation.setReceiverId(listData.get("receiverId") + "");
                                conversation.setStatus(listData.get("status") + "");
                                conversation.setStatusOnline("0");
                                conversation.setUnread(listData.get("unread") + "");
                                conversation.setShopId(listData.get("shopId") + "");
                                conversation.setShopName(listData.get("shopName") + "");
                                conversation.setBuyerId(listData.get("buyerId") + "");
                                conversation.setCountShop(listData.get("countShop") + "");
                                listConversation.add(conversation);
                                Log.e("data frag", "data frag:" + child.getChildrenCount());
//                                keyConversation.add(chidData.getKey());
                            } else if (listData.get("datePost") != null) {
                                Object datePost = listData.get("datePost");
                                conversation.setDatePost(datePost.toString());
                                conversation.setLastMessage(listData.get("lastMessage") + "");
                                conversation.setImageReceiver(listData.get("imageReceiver") + "");
                                conversation.setImageSender(listData.get("imageSender") + "");
                                conversation.setReceiverName(listData.get("receiverName") + "");
                                conversation.setSenderName(listData.get("senderName") + "");
                                conversation.setSenderId(listData.get("senderId") + "");
                                conversation.setReceiverId(listData.get("receiverId") + "");
                                conversation.setStatusOnline("0");
                                conversation.setUnread(listData.get("unread") + "");
                                conversation.setShopId(listData.get("shopId") + "");
                                conversation.setShopName(listData.get("shopName") + "");
                                conversation.setBuyerId(listData.get("buyerId") + "");
                                conversation.setCountShop(listData.get("countShop") + "");
                                listConversation.add(conversation);
                                Log.e("data frag", "data frag:" + child.getChildrenCount());
                            }
                        }
                        checkNoData();
                    }

//                    adapter = new ListChatAdapter(self, listConversation,keyConversation);
                    Collections.sort(listConversation, new Comparator<Conversation>() {
                        @Override
                        public int compare(Conversation o1, Conversation o2) {
                            int value = Integer.parseInt(o2.getDatePost()) - Integer.parseInt(o1.getDatePost());
                            if (value != 0) {
                                return value;
                            }
                            value = Integer.parseInt(o2.getUnread()) - Integer.parseInt(o1.getUnread());
                            if (value != 0) {
                                return value;
                            }
//                            value = o1.getSenderName().compareTo(o2.getSenderName());
                            return value;
                        }
                    });


                    adapter = new ListChatAdapter(self, listConversation);
                    lsvAgent.setAdapter(adapter);
                    checkNoData();

                    ref.child("user").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                for (int i = 0; i < listConversation.size(); i++) {
                                    if (userObj != null) {
                                        if (!userObj.getId().equals(listConversation.get(i).getReceiverId())) {
                                            if (child.getKey().equals(listConversation.get(i).getReceiverId())) {
                                                Map<String, Object> datas = (Map<String, Object>) child.getValue();
                                                Log.e("data", "data user:" + datas.toString());
                                                if (datas.get("status") != null) {
                                                    listConversation.get(i).setStatusOnline(datas.get("status").toString());
                                                }
                                                adapter.notifyDataSetChanged();
                                                checkNoData();
                                            }
                                        } else {
                                            if (child.getKey().equals(listConversation.get(i).getSenderId())) {
                                                Map<String, Object> datas = (Map<String, Object>) child.getValue();
                                                Log.e("data", "data user:" + datas.toString());
                                                if (datas.get("status") != null) {
                                                    listConversation.get(i).setStatusOnline(datas.get("status").toString());
                                                }
                                                adapter.notifyDataSetChanged();
                                                checkNoData();
                                            }
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                    checkNoData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        } else {
            if (listConversation != null) {
                adapter.notifyDataSetChanged();
                checkNoData();
            }
        }
        lsvAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userObj != null) {
                    if (listConversation.get(position).getReceiverId().equals(userObj.getId())
                            && listConversation.get(position).getSenderId().equals(userObj.getId())) {
//                        Toast.makeText(self, getString(R.string.can_not_chat_myself), Toast.LENGTH_SHORT).show();
                    } else {
                        if (NetworkUtil.checkNetworkAvailable(self)) {
                            Intent intent = new Intent(ListChatActivity.this, ChatDetailActivity.class);
                            if (listConversation.get(position).getReceiverId().equals(userObj.getId())) {
                                intent.putExtra("idAgent", listConversation.get(position).getSenderId());
                                intent.putExtra("title", listConversation.get(position).getSenderName());
                                intent.putExtra("image", listConversation.get(position).getImageSender());
                                intent.putExtra(Constant.SHOP_ID, listConversation.get(position).getShopId());
                                intent.putExtra(Constant.SHOP_NAME, listConversation.get(position).getShopName());
                                intent.putExtra(Constant.COUNT_SHOP, listConversation.get(position).getCountShop());
                            } else {
                                intent.putExtra("idAgent", listConversation.get(position).getReceiverId());
                                intent.putExtra("title", listConversation.get(position).getReceiverName());
                                intent.putExtra("image", listConversation.get(position).getImageReceiver());
                                intent.putExtra(Constant.SHOP_ID, listConversation.get(position).getShopId());
                                intent.putExtra(Constant.SHOP_NAME, listConversation.get(position).getShopName());
                                intent.putExtra(Constant.COUNT_SHOP, listConversation.get(position).getCountShop());
                            }
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
                        } else {
                            showToastMessage(getResources().getString(R.string.no_connection));
                        }

                    }
                } else {
                    showToastMessage(getString(R.string.please_relogin_to_use_this_function));
                }
            }
        });

    }

    private void checkNoData() {
        if (listConversation == null || listConversation.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else
            tvNoData.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(broadcastReceiver);
    }

}
