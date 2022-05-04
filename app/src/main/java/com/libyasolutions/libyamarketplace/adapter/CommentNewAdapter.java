package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Comment;

import java.util.ArrayList;

public class CommentNewAdapter extends RecyclerView.Adapter<CommentNewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Comment> commentArrayList;

    public CommentNewAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_comment_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("HUY", "onBindViewHolder: " + commentArrayList.get(0).getUserName());
        holder.tvName.setText(commentArrayList.get(position).getUserID());
        holder.tvTime.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(String.valueOf(commentArrayList.get(position).getCreatedDate())) * 1000) + "");
        holder.tvDescription.setText(commentArrayList.get(position).getContent());
        holder.ratingBar.setRating(Float.parseFloat(Math
                .floor(commentArrayList.get(position).getRateValue() / 4) + ""));

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.layoutItemComment.getLayoutParams();
//        if (position % 2 == 0) {
//            layoutParams.setMargins(0,
//                    0,
//                    (int) context.getResources().getDimension(R.dimen.dimen16_2),
//                    (int) context.getResources().getDimension(R.dimen.dimen16));
//            holder.layoutItemComment.setLayoutParams(layoutParams);
//        } else {
//            layoutParams.setMargins((int) context.getResources().getDimension(R.dimen.dimen16_2),
//                    0,
//                    0,
//                    (int) context.getResources().getDimension(R.dimen.dimen16));
//            holder.layoutItemComment.setLayoutParams(layoutParams);
//        }


    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription, tvTime;
        private RatingBar ratingBar;
        //private RelativeLayout layoutItemComment;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);
            ratingBar = itemView.findViewById(R.id.rtb_user);
            // layoutItemComment = itemView.findViewById(R.id.layoutItemComment);
        }
    }
}
