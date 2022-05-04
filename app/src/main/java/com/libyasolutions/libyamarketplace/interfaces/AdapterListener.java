package com.libyasolutions.libyamarketplace.interfaces;

import android.view.View;

/**
 * Created by GL62 on 8/3/2017.
 */

public class AdapterListener {
    public interface onLoad {
        void onLoad();
    }

    public interface onClickFollow {
        void onclick(String agentID, String type, int index);
    }

    public interface onItemClickListener {
        void onclick(View view, int position);
    }

    public interface onItemClickListenerV3 {
        void onClick(View view, int position, String menuId, String departmentPosition, int clicked);
    }

}
