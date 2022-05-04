package com.libyasolutions.libyamarketplace.util;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.libyasolutions.libyamarketplace.config.PreferencesManager;

/**
 * Created by GL62 on 6/7/2017.
 */

public class FirebaseUtils {


    public static void changeStatus(Context context, String status) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        if (PreferencesManager.getInstance(context).getUserInfo() != null) {
            ref.child("user").child(PreferencesManager.getInstance(context).getUserInfo().getUser_id()).child("status").setValue(status);
        }
    }
}
