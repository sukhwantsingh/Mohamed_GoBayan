package com.libyasolutions.libyamarketplace.modelmanager;

import com.android.volley.VolleyError;

public interface ModelManagerListener {
	public void onError(VolleyError error);

	public void onSuccess(Object object);
}
