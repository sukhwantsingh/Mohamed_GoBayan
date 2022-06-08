package com.libyasolutions.libyamarketplace.adapter;

/**
 * Created by Administrator on 5/20/2017.
 */

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.ChatDetailActivity;
import com.libyasolutions.libyamarketplace.activity.FullImageActivity;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.object.MessengerFirebase;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshListView;
import com.libyasolutions.libyamarketplace.util.DateTimeUtility;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import me.himanshusoni.chatmessageview.ChatMessageView;

public class ChatAdapter extends BaseAdapter implements InternetConnectivityListener {

    private ArrayList<MessengerFirebase> arrRealEstate;
    private LayoutInflater inflater = null;
    private ChatDetailActivity mAct;
    private AQuery mAq;
    DatabaseReference ref;
    private String keyMessage = "";
    private String image = "";
    public int mPageLimit = 20;

    private String lastSended = "";
    private String idAgent;
    private int countMessage = 0;
    private String lastKey = "";

    public boolean isLoadmore = false;
    private AdapterListener.onLoad listener;
    private Account userObj;
    private InternetAvailabilityChecker internetAvailabilityChecker;
    public int currentSize;
    private String buyerId = "";

    public ChatAdapter(ChatDetailActivity activity, final String keyMessage, String image, String _idAgent) {
        mAct = activity;
        this.keyMessage = keyMessage;
        this.image = image;
        internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        this.internetAvailabilityChecker.addInternetConnectivityListener(this);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ref = FirebaseDatabase.getInstance().getReference();
        arrRealEstate = new ArrayList<>();
        this.idAgent = _idAgent;
        this.listener = activity;
        userObj = MySharedPreferences.getInstance(mAct).getUserInfo();
        getData();
    }

    public void getMore(final PullToRefreshListView listView) {

        final ArrayList<MessengerFirebase> arr = new ArrayList<>();
        Query query = ref.child("message").child(keyMessage).orderByKey().endAt(lastKey).limitToLast(mPageLimit);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        MessengerFirebase messengerFirebase = new MessengerFirebase();
                        Map<String, Object> map = (Map<String, Object>) datas.getValue();
                        String userId = userObj.getId() + "";
                        if (map != null) {
                            messengerFirebase.setDatePost(map.get("datePost") + "");
                            messengerFirebase.setMessage(map.get("message") + "");
                            messengerFirebase.setReceiverId(map.get("receiverId") + "");
                            messengerFirebase.setSenderId(map.get("senderId") + "");
                            messengerFirebase.setUrlFile(map.get("urlFile") + "");
                            messengerFirebase.setImageH(map.get("imageH") + "");
                            messengerFirebase.setImageW(map.get("imageW") + "");
                            if (messengerFirebase.getReceiverId().equals(userId) || messengerFirebase.getSenderId().equals(userId)) {
                                arr.add(0, messengerFirebase);
                                if (arr.size() == 1) {
                                    lastKey = datas.getKey();
                                }

                            }
                        }
                    }
                    Log.e("kevin", "arr size: " + arr.size());
                    arr.remove(0);
                    if (arr.size() == 0) {
                        isLoadmore = true;
                    } else {
                        currentSize = arr.size();
                        isLoadmore = false;
                    }
                    Log.e("kevin", "current size: " + currentSize);
                    Collections.sort(arr, new Comparator<MessengerFirebase>() {
                        @Override
                        public int compare(MessengerFirebase o1, MessengerFirebase o2) {
                            return Integer.parseInt(o1.getDatePost()) - Integer.parseInt(o2.getDatePost());
                        }
                    });
//                    Collections.reverse(arr);
                    arrRealEstate.addAll(0, arr);
//                    if (arrRealEstate.size() < 19) {
//                        isLoadmore = false;
//                        countMessage = arrRealEstate.size();
//                    } else {
//                        isLoadmore = true;
//                    }

                    notifyDataSetChanged();
                    listView.onRefreshComplete();
                    listView.getRefreshableView().setSelection(arr.size());

                } else {
                    Toast toast = Toast.makeText(mAct, mAct.getResources().getString(R.string.no_message), Toast.LENGTH_SHORT);
                    toast.show();
                    isLoadmore = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void getMoreData(PullToRefreshListView listView) {
        if (!isLoadmore) {
            isLoadmore = true;
            getMore(listView);
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public static <T> T getLast(ArrayList<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }

    public void getData() {
        if (!keyMessage.equals("")) {
            Query query = ref.child("message").child(keyMessage).limitToLast(mPageLimit);

            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.hasChildren()) {
                        if (userObj != null) {
                            MessengerFirebase messengerFirebase = dataSnapshot.getValue(MessengerFirebase.class);
                            if (!NetworkUtil.checkNetworkAvailable(mAct)/* || (NetworkUtil.checkNetworkAvailable(mAct)) && !NetworkUtil.isOnline()*/) {
                                messengerFirebase.setSending(true);
                            }
                            String userId = userObj.getId() + "";
                            if (messengerFirebase != null) {
                                if (messengerFirebase.getReceiverId().equals(userId) || messengerFirebase.getSenderId().equals(userId)) {
                                    arrRealEstate.add(messengerFirebase);
                                    notifyDataSetChanged();
                                    if (arrRealEstate.size() != 0)
                                        setBuyerId(arrRealEstate.get(0).getBuyerId());
                                    listener.onLoad();
                                    if (arrRealEstate.size() == 1) {
                                        lastKey = dataSnapshot.getKey();
                                    }
                                }

                            }

                        }

                    } else {
                        Toast.makeText(mAct, "No message", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.hasChildren()) {
                        MessengerFirebase messengerFirebase = dataSnapshot.getValue(MessengerFirebase.class);
                        if (userObj != null) {
                            String userId = userObj.getId() + "";
                            if (messengerFirebase != null) {
                                if (messengerFirebase.getReceiverId().equals(userId) || messengerFirebase.getSenderId().equals(userId)) {
                                    arrRealEstate.add(messengerFirebase);
                                    notifyDataSetChanged();
                                    if (arrRealEstate.size() != 0)
                                        setBuyerId(arrRealEstate.get(0).getBuyerId());
                                    listener.onLoad();
                                    if (arrRealEstate.size() == 1) {
                                        lastKey = dataSnapshot.getKey();
                                    }
                                }

                            }
                        }

                    } else {
                        Toast toast = Toast.makeText(mAct, mAct.getResources().getString(R.string.no_message), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        notifyDataSetChanged();
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


    @Override
    public int getCount() {
        try {
            return arrRealEstate.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public static <T> T getLastElement(final Iterable<T> elements) {
        final Iterator<T> itr = elements.iterator();
        T lastElement = itr.next();

        while (itr.hasNext()) {
            lastElement = itr.next();
        }
        return lastElement;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chat_sender, null);
            holder = new HolderView();
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        holder.txtMessage = convertView.findViewById(R.id.txtMessage);
        holder.txtMessageRight = convertView.findViewById(R.id.txtMessageRight);

        holder.chatLeft = convertView.findViewById(R.id.chatLeft);
        holder.chatRight = convertView.findViewById(R.id.chatRight);

        holder.chatRightContainer = convertView.findViewById(R.id.chatRightContainer);
        holder.chatLeftContainer = convertView.findViewById(R.id.chatLeftContainer);

        holder.imgAvatarLeft = convertView.findViewById(R.id.imgAvatarLeft);
        holder.imgAvatarRight = convertView.findViewById(R.id.imgAvatarRigt);

        holder.txtDateRight = convertView.findViewById(R.id.txtDateRight);
        holder.txtDateLeft = convertView.findViewById(R.id.txtDateLeft);

        holder.imgLeft = convertView.findViewById(R.id.imgLeft);
        holder.imgRight = convertView.findViewById(R.id.imgRight);


        if (getCount() > 0) {
            final MessengerFirebase o = arrRealEstate.get(position);
            Glide.with(mAct).load(userObj.getAvatar()).asBitmap().into(holder.imgAvatarLeft);
            Glide.with(mAct).load(image).asBitmap().into(holder.imgAvatarRight);

            if (o.getSenderId().equals(userObj.getId())) {
                holder.chatLeftContainer.setVisibility(View.VISIBLE);
                holder.chatRightContainer.setVisibility(View.GONE);
                holder.imgAvatarLeft.setVisibility(View.VISIBLE);
                holder.imgAvatarRight.setVisibility(View.GONE);


            } else {
                holder.imgAvatarRight.setVisibility(View.VISIBLE);
                holder.imgAvatarLeft.setVisibility(View.GONE);
                holder.chatLeftContainer.setVisibility(View.GONE);
                holder.chatRightContainer.setVisibility(View.VISIBLE);
            }

            //if is Files
//            if (!o.getUrlFile().equals("")) {
//                if (getFileType(o.getUrlFile())) {
//                    holder.txtMessage.setVisibility(View.GONE);
//                    holder.txtMessageRight.setVisibility(View.GONE);
//
//                    //is message image
//                    holder.imgLeft.setVisibility(View.VISIBLE);
//                    holder.imgRight.setVisibility(View.VISIBLE);
//
//                    Log.d("ANH", "getView: :" + o.getUrlFile());
////                    Glide.with(mAct).load(WebServiceConfig.FILES_API + o.getUrlFile()).asBitmap().into(holder.imgLeft);
//                    if (o.getSenderId().equals(userObj.getId())) {
//                        int w = (int) Double.parseDouble(o.getImageW());
//                        int h = (int) Double.parseDouble(o.getImageH());
//                        ImageUtil.mycalViewRatio(mAct, holder.imgLeft, w, h);
//                    } else {
//                        int w = (int) Double.parseDouble(o.getImageW());
//                        int h = (int) Double.parseDouble(o.getImageH());
//                        ImageUtil.mycalViewRatio(mAct, holder.imgRight, w, h);
//                    }
//                    Glide.with(mAct).load(WebServiceConfig.FILES_API + o.getUrlFile()).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<GlideDrawable>() {
//                        @Override
//                        public void onStart() {
//                            super.onStart();
//                            holder.imgLeft.setImageBitmap(null);
//                            holder.chatLeft.setBackgroundColors(R.color.bgGuest, R.color.bgGuest);
//                        }
//
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            holder.imgLeft.setImageDrawable(resource);
//                            holder.chatLeft.setBackgroundColors(R.color.transparent, R.color.transparent);
//                        }
//                    });
//
//                    Glide.with(mAct).load(WebServiceConfig.FILES_API + o.getUrlFile()).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<GlideDrawable>() {
//                        @Override
//                        public void onStart() {
//                            super.onStart();
//                            holder.imgRight.setImageBitmap(null);
//                            holder.chatRight.setBackgroundColors(R.color.bgGuest, R.color.bgGuest);
//                        }
//
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            holder.imgRight.setImageDrawable(resource);
//                            holder.chatRight.setBackgroundColors(R.color.transparent, R.color.transparent);
//                        }
//                    });
//                } else {
//                    // is Files
//                    holder.imgLeft.setVisibility(View.GONE);
//                    holder.imgRight.setVisibility(View.GONE);
//                    holder.txtMessage.setVisibility(View.VISIBLE);
//                    holder.txtMessageRight.setVisibility(View.VISIBLE);
//                    holder.txtMessage.setText(Utils.underLine(o.getMessage()));
//                    holder.txtMessageRight.setText(Utils.underLine(o.getMessage()));
//
//                }
//            } else {
            holder.chatLeft.setBackgroundColors(R.color.message, R.color.message);
            holder.chatRight.setBackgroundColors(R.color.bgGuest, R.color.bgGuest);
            holder.imgLeft.setVisibility(View.GONE);
            holder.imgRight.setVisibility(View.GONE);
            holder.txtMessage.setVisibility(View.VISIBLE);
            holder.txtMessageRight.setVisibility(View.VISIBLE);
            holder.txtMessage.setText(o.getMessage());
            holder.txtMessageRight.setText(o.getMessage());
//            }

            //setDate
            holder.txtDateRight.setText(DateTimeUtility.convertTimeStampToDate(o.getDatePost(), "dd.MMM.yyyy HH.mm"));
            if (o.isSending()) {
                holder.txtDateLeft.setText(mAct.getResources().getString(R.string.sending));
            } else {
                holder.txtDateLeft.setText(DateTimeUtility.convertTimeStampToDate(o.getDatePost(), "dd.MMM.yyyy HH.mm"));
            }

            holder.chatLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!o.getUrlFile().equals("")) {
                        if (getFileType(o.getUrlFile())) {
                            if (holder.imgLeft.getDrawable() != null) {
                                Intent i = new Intent(mAct, FullImageActivity.class);
                                Bundle b = new Bundle();
                                b.putString("url", o.getUrlFile());
                                i.putExtra("Image", b);
                                mAct.startActivity(i);
                                mAct.overridePendingTransition(R.anim.fadein,
                                        R.anim.fadeout);
                            } else {
                                Toast toast = Toast.makeText(mAct, mAct.getResources().getString(R.string.wait_tp_load_image), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } else {
                            String url = WebServiceConfig.FILES_API + o.getUrlFile();
                            try {
                                Uri uri = Uri.parse(url);
                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mAct.startActivity(i);
                            } catch (ActivityNotFoundException e) {
                                Toast toast = Toast.makeText(mAct, mAct.getResources().getString(R.string.install_chrome), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }

                    } else {
                        if (holder.txtDateLeft.getVisibility() == View.VISIBLE) {
                            holder.txtDateLeft.setVisibility(View.GONE);
                        } else if (holder.txtDateLeft.getVisibility() == View.GONE) {
                            holder.txtDateLeft.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            holder.chatRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!o.getUrlFile().equals("")) {
                        if (getFileType(o.getUrlFile())) {
                            if (holder.imgRight.getDrawable() != null) {
                                Intent i = new Intent(mAct, FullImageActivity.class);
                                Bundle b = new Bundle();
                                b.putString("url", o.getUrlFile());
                                i.putExtra("Image", b);
                                mAct.startActivity(i);
                                mAct.overridePendingTransition(R.anim.fadein,
                                        R.anim.fadeout);
                            } else {
                                Toast toast = Toast.makeText(mAct, mAct.getResources().getString(R.string.wait_tp_load_image), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } else {
                            String url = WebServiceConfig.FILES_API + o.getUrlFile();
                            try {
                                Uri uri = Uri.parse(url);
                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mAct.startActivity(i);
                            } catch (ActivityNotFoundException e) {
                                Toast toast = Toast.makeText(mAct, mAct.getResources().getString(R.string.install_chrome), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }

                    } else {
                        if (holder.txtDateRight.getVisibility() == View.VISIBLE) {
                            holder.txtDateRight.setVisibility(View.GONE);
                        } else if (holder.txtDateRight.getVisibility() == View.GONE) {
                            holder.txtDateRight.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            //Visible If LastMess is previous SendedID
            if (position != 0) {
                lastSended = arrRealEstate.get(position - 1).getSenderId();
                if (lastSended.equals(o.getSenderId())) {
                    holder.imgAvatarLeft.setVisibility(View.GONE);
                    holder.imgAvatarRight.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }

    @Override
    public void onInternetConnectivityChanged(boolean b) {
        for (MessengerFirebase message : arrRealEstate) {
            if (message.isSending()) {
                message.setSending(false);
            }
        }
        notifyDataSetChanged();
    }

    public class HolderView {
        TextView txtMessage, txtMessageRight, txtDateRight, txtDateLeft;
        ChatMessageView chatLeft, chatRight;
        ImageView imgAvatarLeft, imgAvatarRight, imgLeft, imgRight;
        RelativeLayout chatLeftContainer, chatRightContainer;
    }

    public boolean getFileType(String filesname) {
        if (filesname.endsWith(".jpg")) {
            return true;
        } else if (filesname.endsWith(".png")) {
            return true;
        } else if (filesname.endsWith(".jpeg")) {
            return true;
        } else {
            return false;
        }
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    private void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerId() {
        return buyerId;
    }
}
