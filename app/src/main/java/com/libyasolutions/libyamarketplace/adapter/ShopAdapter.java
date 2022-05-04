package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Shop;

import java.util.ArrayList;

public class ShopAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Shop> lsvShop;
    private static LayoutInflater inflater = null;
    private AQuery aq;
    private static final String TAG = "SHOP ADAPTER";

    public ShopAdapter(Activity mcontext, ArrayList<Shop> arrOffer) {
        context = mcontext;
        lsvShop = arrOffer;
        inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        aq = new AQuery(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lsvShop.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Hoder holder;
        if (convertView == null) {
            holder = new Hoder();
            convertView = inflater.inflate(R.layout.row_list_shop, null);
            holder.imgShop = (ImageView) convertView.findViewById(R.id.imgShop);
            holder.progress = (ProgressBar) convertView
                    .findViewById(R.id.progess);
            holder.lblShopName = (TextView) convertView
                    .findViewById(R.id.lblShopName);
            holder.lblOpenhourStatus = (TextView) convertView
                    .findViewById(R.id.lblOpenHourStatus);
            holder.imgFeatured = (ImageView) convertView
                    .findViewById(R.id.imgFeatured);
            holder.imgVerified = (ImageView) convertView
                    .findViewById(R.id.imgVerified);
            holder.rtbRating = (RatingBar) convertView
                    .findViewById(R.id.rtbRating);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.lblCategoryName = (TextView) convertView.findViewById(R.id.lblCategoryName);
            holder.address.setSelected(true);

            convertView.setTag(holder);

        } else {
            holder = (Hoder) convertView.getTag();
        }
        final Shop o = lsvShop.get(position);
        if (o != null) {

            if (o.isOpen()) {
                holder.lblOpenhourStatus.setVisibility(View.GONE);
            } else {
                aq.id(holder.lblOpenhourStatus).text(context.getString(R.string.closed).toUpperCase());
                holder.lblOpenhourStatus.setVisibility(View.VISIBLE);
            }
            aq.id(holder.lblShopName).text(o.getShopName());
            if (o.isFeatured()) {
                holder.imgFeatured.setVisibility(View.VISIBLE);
            } else {
                holder.imgFeatured.setVisibility(View.INVISIBLE);
            }

            if (o.isVerified()) {
                holder.imgVerified.setVisibility(View.VISIBLE);
            } else {
                holder.imgVerified.setVisibility(View.INVISIBLE);
            }

            holder.address.setText(o.getAddress());
            holder.lblCategoryName.setText(o.getCategory());

            holder.rtbRating.setRating(o.getRateValue() / 2);
            Glide.with(context).load(o.getImage()).into(holder.imgShop);
//            aq.id(holder.imgShop)
//                    .progress(holder.progress)
//                    .image(o.getImage(), false, true, 0,
//                            R.drawable.no_image_available_horizontal,
//                            new BitmapAjaxCallback() {
//                                @SuppressWarnings("deprecation")
//                                @Override
//                                public void callback(String url, ImageView iv,
//                                                     Bitmap bm, AjaxStatus status) {
//                                    if (bm != null) {
//                                        Drawable d = new BitmapDrawable(context
//                                                .getResources(), ImageUtil
//                                                .getResizedBitmap(bm, 200, 200));
//                                        iv.setBackgroundDrawable(d);
//                                    }
//                                }
//                            });
        }
        return convertView;
    }


    static class Hoder {
        ImageView imgShop;
        ProgressBar progress;
        TextView lblShopName, lblOpenhourStatus,
                address,lblCategoryName;
        ImageView imgFeatured, imgVerified;
        RatingBar rtbRating;

    }

}
