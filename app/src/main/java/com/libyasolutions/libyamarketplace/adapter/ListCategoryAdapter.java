package com.libyasolutions.libyamarketplace.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import com.libyasolutions.libyamarketplace.object.Category;

public class ListCategoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Category> arrCategories;
    private static LayoutInflater inflater = null;
    private AQuery aq;

    public ListCategoryAdapter(Context mcontext, ArrayList<Category> arrCategory) {
        context = mcontext;
        arrCategories = arrCategory;
        inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        aq = new AQuery(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrCategories.size();
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
            convertView = inflater.inflate(R.layout.items_category_new,null);
//            if (position % 2 == 0) {
//                convertView = inflater.inflate(R.layout.row_list_category, null);
//            } else {
//                convertView = inflater.inflate(R.layout.row_list_category_right, null);
//            }

            holder.imgCategory = (ImageView) convertView.findViewById(R.id.imgCategory);
            holder.lblCategoryName = (TextView) convertView.findViewById(R.id.lblCategoryName);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.progess);
            convertView.setTag(holder);

        } else {
            holder = (Hoder) convertView.getTag();
        }
        final Category o = arrCategories.get(position);
        if (o != null) {
            aq.id(holder.lblCategoryName).text(o.getName());
            aq.id(holder.imgCategory)
                    .progress(holder.progress)
                    .image(o.getImage(), true, true, 0,
                            R.drawable.no_image_available_horizontal,
                            new BitmapAjaxCallback() {
                                @SuppressLint("NewApi")
                                @Override
                                public void callback(String url, ImageView iv,
                                                     Bitmap bm, AjaxStatus status) {
                                    if (bm != null) {
                                        Drawable d = new BitmapDrawable(context.getResources(), bm);
                                        holder.imgCategory.setBackgroundDrawable(d);
                                    } else {
                                        holder.imgCategory
                                                .setBackgroundResource(R.drawable.no_image_available_horizontal);
                                    }
                                }
                            });
        }
        return convertView;
    }

    static class Hoder {
        ImageView imgCategory;
        ProgressBar progress;
        TextView lblCategoryName;
    }
}
