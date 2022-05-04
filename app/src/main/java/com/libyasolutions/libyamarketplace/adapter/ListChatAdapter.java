package com.libyasolutions.libyamarketplace.adapter;

/**
 * Created by Administrator on 5/30/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.PreferencesManager;
import com.libyasolutions.libyamarketplace.object.Conversation;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 5/20/2017.
 */

public class ListChatAdapter extends BaseAdapter {

    public ArrayList<Conversation> arrRealEstate;
    private LayoutInflater inflater = null;
    private Activity mAct;
    private DecimalFormat dec = new DecimalFormat("₦#,###");


    //Count message
//    private ArrayList<String> arlKeyconvesation;
//    private int count = 0;

    public ListChatAdapter(Activity activity, ArrayList<Conversation> listEstate) {
//        this.arlKeyconvesation = _key;
        arrRealEstate = listEstate;
        mAct = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        final HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chat, null);
            holder = new HolderView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        holder.tvCountMessage.setVisibility(View.GONE);
        if (getCount() > 0) {
            final Conversation o = arrRealEstate.get(position);
            holder.txtDate.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(String.valueOf(o.getDatePost())) * 1000) + "");
            if (o.getReceiverId().equals(MySharedPreferences.getInstance(mAct).getUserInfo().getId())) {
                holder.txtContent.setText(o.getSenderName());
                Glide.with(mAct).load(o.getImageSender()).asBitmap().into(holder.imageAvatar);
            } else {
                holder.txtContent.setText(o.getReceiverName());
                Glide.with(mAct).load(o.getImageReceiver()).asBitmap().into(holder.imageAvatar);

            }
//            if (o.getStatusOnline().equals("0")) {
//                holder.imgStatus.setImageResource(R.drawable.ic_offline);
//            } else if (o.getStatusOnline().equals("2")) {
//                holder.imgStatus.setImageResource(R.drawable.ic_aways);
//            } else {
//                holder.imgStatus.setImageResource(R.drawable.ic_online);
//            }

            if (!o.getSenderId().equals(MySharedPreferences.getInstance(mAct).getUserInfo().getId())) {
                if (!"0".equals(o.getUnread())) {
                    if (Integer.valueOf(o.getUnread()) > 5) {
                        holder.tvCountMessage.setText("5+");
                        holder.tvCountMessage.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvCountMessage.setText(o.getUnread());
                        holder.tvCountMessage.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                holder.tvCountMessage.setVisibility(View.GONE);
            }
            holder.txtNewMsg.setText(o.getLastMessage());
        }


        return convertView;
    }

    public static class HolderView {
        CircleImageView imageAvatar;
        TextView txtContent, txtNewMsg, txtDate;
        ImageView imgStatus, imgStatusNew;
        TextView tvCountMessage;

        public HolderView (View convertView){
            imageAvatar =  convertView.findViewById(R.id.imgAvatar);
            imgStatus =  convertView.findViewById(R.id.imgStatus);
            imgStatusNew =  convertView.findViewById(R.id.imgStatusNew);
            txtDate =  convertView.findViewById(R.id.txtDate);
            txtContent =  convertView.findViewById(R.id.txtContent);
            txtNewMsg =  convertView.findViewById(R.id.txtNewMsg);

            //This For Count Message
            tvCountMessage =  convertView.findViewById(R.id.tvCountMessage);
        }
    }


}

