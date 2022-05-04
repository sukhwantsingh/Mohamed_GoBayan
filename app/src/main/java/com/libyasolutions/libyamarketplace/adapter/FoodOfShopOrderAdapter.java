package com.libyasolutions.libyamarketplace.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Menu;

public class FoodOfShopOrderAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Menu> lsvShop;
    private static LayoutInflater inflater = null;
    private AQuery aq;

    public FoodOfShopOrderAdapter(Activity mcontext, ArrayList<Menu> arrOffer) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Hoder holder;
        if (convertView == null) {
            holder = new Hoder();
            convertView = inflater.inflate(
                    R.layout.row_list_food_of_shop_order, null);
            holder.imgFood = (ImageView) convertView.findViewById(R.id.imgFood);
            holder.progress = (ProgressBar) convertView
                    .findViewById(R.id.progess);
            holder.lblFoodName = (TextView) convertView
                    .findViewById(R.id.lblFoodName);
            holder.lblFoodNumber = (TextView) convertView
                    .findViewById(R.id.lblFoodNumber);
            holder.lblPrice = (TextView) convertView
                    .findViewById(R.id.lblPrice);

            convertView.setTag(holder);

        } else {
            holder = (Hoder) convertView.getTag();
        }
        final Menu o = lsvShop.get(position);
        if (o != null) {

            aq.id(holder.lblFoodName).text(o.getName());

//            aq.id(holder.lblFoodNumber).text("x" +
//                    String.format("%02d", o.getOrderNumber()));
            aq.id(holder.lblFoodNumber).text("x" + o.getOrderNumber());
            aq.id(holder.lblPrice).text(
                    "" + o.getPrice());

            aq.id(holder.imgFood)
                    .progress(holder.progress)
                    .image(o.getImage(), true, true, 0,
                            R.drawable.no_image_available_horizontal,
                            new BitmapAjaxCallback() {
                                @SuppressWarnings("deprecation")
                                @Override
                                public void callback(String url, ImageView iv,
                                                     Bitmap bm, AjaxStatus status) {

                                    Drawable d = new BitmapDrawable(context
                                            .getResources(), bm);
                                    holder.imgFood.setBackgroundDrawable(d);
                                }
                            });

        }
        return convertView;
    }

    static class Hoder {
        ImageView imgFood;
        ProgressBar progress;
        TextView lblFoodName, lblPrice, lblFoodNumber;

    }

}
