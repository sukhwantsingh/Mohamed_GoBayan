package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.ShopDetailActivity;
import com.libyasolutions.libyamarketplace.activity.ShopDetailsNew;
import com.libyasolutions.libyamarketplace.adapter.ShopAdapter;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.object.Shop;

import java.util.ArrayList;

@SuppressLint("NewApi")
public final class FavoriteShopFragment extends Fragment implements OnClickListener {

	public Activity act;
	private View view;
	private ListView listView;
	public ArrayList<Shop> arrayList;
	private ShopAdapter shopAdapter;

	public static FavoriteShopFragment setInstance(Activity act,ArrayList<Shop> arrShop) {
		FavoriteShopFragment fragment = new FavoriteShopFragment();
		fragment.act=act;
		fragment.arrayList = arrShop;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_favorite_list, null);
		listView=(ListView)view.findViewById(R.id.listView);
		shopAdapter=new ShopAdapter(act,arrayList);
		listView.setAdapter(shopAdapter);
		listView.setOnItemClickListener((adapterView, view, i, l) -> {
			Shop shop = arrayList.get(i);

			Bundle bundle = new Bundle();
			bundle.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
			Intent intent = new Intent(requireActivity(), ShopDetailsNew.class);
			intent.putExtras(bundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

//			Shop shop=arrayList.get(i);
//			Bundle bundle = new Bundle();
//			bundle.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
//			((BaseActivity)act).gotoActivity(act, ShopDetailActivity.class, bundle);
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
