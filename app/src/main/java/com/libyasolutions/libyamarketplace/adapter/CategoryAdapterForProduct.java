package com.libyasolutions.libyamarketplace.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Category;

import java.util.ArrayList;

public class CategoryAdapterForProduct extends ArrayAdapter<Category> {

	Activity act;
	private ArrayList<Category> list;
	private LayoutInflater inflate;
	private int selectRow = -1;

	public CategoryAdapterForProduct(Activity context, int resource,
                                     ArrayList<Category> objects) {
		super(context, resource, objects);
		act = context;
		list = objects;
		inflate = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = inflate.inflate(R.layout.item_list_category_for_product, parent, false);
		TextView make = (TextView) row.findViewById(R.id.lblTextContent);
		Category o = list.get(position);
		make.setText(o.getName());
		selectRow = position;
		return row;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflate.inflate(R.layout.layout_text_row_spinner,
					null);
			holder.lblTextcontent = (TextView) convertView
					.findViewById(R.id.lblTextContent);
			holder.layoutRowSpinner = (RelativeLayout) convertView
					.findViewById(R.id.layoutRowSpinner);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Category o = list.get(position);

		if (o != null) {
			if (selectRow == position) {
				holder.layoutRowSpinner.setBackgroundColor(act.getResources()
						.getColor(R.color.red_button));
				holder.lblTextcontent.setTextColor(Color.WHITE);

			} else {
				holder.lblTextcontent.setTextColor(Color.BLACK);
				holder.layoutRowSpinner.setBackgroundColor(Color.WHITE);
			}
			holder.lblTextcontent.setText(o.getName());
		}
		return convertView;
	}

	class ViewHolder {
		TextView lblTextcontent;
		RelativeLayout layoutRowSpinner;
	}

}
