package com.libyasolutions.libyamarketplace.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.util.ImageUtil;

public class ListFoodAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Menu> arrFood;
    private static LayoutInflater inflater = null;
    private AQuery aq;
    public Boolean showShop;

    public ListFoodAdapter(Activity mcontext, ArrayList<Menu> arr) {
        context = mcontext;
        arrFood = arr;
        inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        aq = new AQuery(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrFood.size();
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
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.row_list_food, null);
            holder.imgFood = (ImageView) convertView.findViewById(R.id.imgProduct);
            holder.imgShop = (ImageView) convertView.findViewById(R.id.imgShop);
            holder.progress = (ProgressBar) convertView
                    .findViewById(R.id.progess);
            holder.lblFoodName = (TextView) convertView
                    .findViewById(R.id.lblProductName);
            holder.lblPrice = (TextView) convertView
                    .findViewById(R.id.lblPrice);
            holder.lblShopName = (TextView) convertView
                    .findViewById(R.id.lblShopName);
            holder.lblDiscountPrice = (TextView) convertView
                    .findViewById(R.id.lblDiscountPrice);
            holder.rtbRating = (RatingBar) convertView
                    .findViewById(R.id.rtbRating);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.address.setSelected(true);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }
        final Menu o = arrFood.get(position);
        if (o != null) {
            holder.rtbRating.setRating((o.getRateValue() / 2));
            holder.lblPrice.setText(context.getString(R.string.currency)
                    + String.format("%.1f", o.getPrice()));
            holder.lblFoodName.setText(o.getName());
            holder.lblDiscountPrice.setText(context
                    .getString(R.string.currency)
                    + String.format("%.1f", o.getCurrentPrice()));
            holder.lblDiscountPrice.setTextColor(Color.RED);
            holder.lblPrice.setTextColor(Color.GRAY);
            holder.lblPrice.setPaintFlags(holder.lblPrice.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.address.setText(o.getShop().getAddress());
            holder.lblShopName.setText(o.getShop().getShopName());

            if (o.getPercentDiscount() > 0) {
                holder.lblPrice.setVisibility(View.VISIBLE);
            } else {
                holder.lblPrice.setVisibility(View.GONE);
            }

            aq.id(holder.imgFood)
                    .progress(holder.progress)
                    .image(o.getImage(), true, true, 0,
                            R.drawable.no_image_available_horizontal,
                            new BitmapAjaxCallback() {
                                @SuppressWarnings("deprecation")
                                @Override
                                public void callback(String url, ImageView iv,
                                                     Bitmap bm, AjaxStatus status) {

                                    if (bm != null) {
                                        Drawable d = new BitmapDrawable(context
                                                .getResources(), ImageUtil
                                                .getResizedBitmap(bm, 200, 200));
                                        iv.setBackgroundDrawable(d);
                                    }
                                }
                            });
            aq.id(holder.imgShop)
                    .image(o.getShop().getThumbnail(), true, true, 0,
                            R.drawable.ic_category,
                            new BitmapAjaxCallback() {
                                @SuppressWarnings("deprecation")
                                @Override
                                public void callback(String url, ImageView iv,
                                                     Bitmap bm, AjaxStatus status) {

                                    if (bm != null) {
                                        int size = ImageUtil.getSizeBaseOnDensity(context, 15);
                                        Drawable d = new BitmapDrawable(context
                                                .getResources(), ImageUtil
                                                .getResizedBitmap(bm, size, size));
                                        iv.setBackgroundDrawable(d);
                                    }
                                }
                            });

        }
        return convertView;
    }

    class Holder {
        ImageView imgFood, imgShop;
        ProgressBar progress;
        TextView lblFoodName, lblShopName, lblPrice, lblDiscountPrice, address;
        RatingBar rtbRating;

    }

}
