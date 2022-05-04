package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Comment;
import com.libyasolutions.libyamarketplace.util.DateTimeUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends BaseAdapter {

    private ArrayList<Comment> arrComment;
    private LayoutInflater inflater;
    private Context context;
    public boolean commentOnly = false;

    public CommentAdapter(Context ctx, ArrayList<Comment> arrComment, boolean commentOnly) {
        this.arrComment = arrComment;
        this.commentOnly = commentOnly;
        if (arrComment.size() == 0) {
            Comment temp = new Comment();
            this.arrComment.add(temp);
        }
        this.context = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        try {
            return arrComment.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_comment, null);
            holder.llComment = (LinearLayout) convertView
                    .findViewById(R.id.ll_comment);
            holder.llComment.setOnClickListener(null);
            holder.lblComment = (TextView) convertView
                    .findViewById(R.id.lbl_comment);
            holder.lblTime = (TextView) convertView.findViewById(R.id.lbl_time);
            holder.lblUser = (TextView) convertView.findViewById(R.id.lbl_user);
            holder.rtb = (RatingBar) convertView.findViewById(R.id.rtb_user);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        Comment cmt = arrComment.get(position);
        if (cmt != null) {
            holder.lblUser.setText(cmt.getUserID());
            holder.lblComment.setText(cmt.getContent());
            Date specDate = DateTimeUtility.getDateValueFromString(
                    "yyyy-MM-dd HH:mm:ss", cmt.getCreatedDate());
            holder.lblTime.setText(convertTimeToCustomDisplay(specDate));
            holder.rtb.setRating((cmt.getRateValue() / 2));
        }
        return convertView;
    }

    private String convertTimeToCustomDisplay(Date specDate) {
        String time = "";
        Date curDate = Calendar.getInstance().getTime();
        long minuteDiff = DateTimeUtility.getDateDiff(curDate, specDate,
                TimeUnit.MINUTES);
        long hourDiff = DateTimeUtility.getDateDiff(curDate, specDate,
                TimeUnit.HOURS);
        long dateDiff = DateTimeUtility.getDateDiff(curDate, specDate,
                TimeUnit.DAYS);

        if (minuteDiff <= 1) {
            time = context.getResources()
                    .getString(R.string.just_now);
        } else if (minuteDiff > 1 && minuteDiff <= 59) {
            time = minuteDiff
                    + " "
                    + context.getResources()
                    .getString(R.string.minutes_ago);
        } else if (minuteDiff > 59) {
            // Reset minute var
            minuteDiff = 0;

            if (hourDiff == 1) {
                time = hourDiff
                        + " "
                        + context.getResources()
                        .getString(R.string.hour_ago);
            } else if (hourDiff < 24) {
                time = hourDiff
                        + " "
                        + context.getResources()
                        .getString(R.string.hours_ago);
            } else {
                // Reset hour var
                hourDiff = 0;

                if (dateDiff == 0) {
                    time = context.getResources()
                            .getString(R.string.today);
                } else if (dateDiff == 1) {
                    time = context.getResources()
                            .getString(R.string.yesterday);
                } else if (dateDiff == 2) {
                    time = context.getResources()
                            .getString(R.string.two_days_ago);
                } else if (dateDiff == 3) {
                    time = context.getResources()
                            .getString(R.string.three_days_ago);
                } else {
                    // Reset date var
                    dateDiff = 0;
                    time = DateTimeUtility.formatDate(
                            specDate,
                            "EEE, dd MMM yyyy");
                }
            }
        }
        return time;
    }

    class Holder {
        private LinearLayout llComment;
        private TextView lblUser, lblComment, lblTime;
        private RatingBar rtb;

    }
}
