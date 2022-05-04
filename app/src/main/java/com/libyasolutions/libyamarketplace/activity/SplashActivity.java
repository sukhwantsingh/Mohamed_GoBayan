package com.libyasolutions.libyamarketplace.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.LoginActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.MainTabActivity;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.util.GPSTracker;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;

public class SplashActivity extends BaseActivity {

    private GPSTracker gps;
    private Handler handler = new Handler();
    private boolean isLoadCity = false, isLoadCategories = false, isLoadAccount = false;
    private MySharedPreferences preferenceManager;

    List<String> listPermission = new ArrayList<>();
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    //check load success
    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (isLoadCity && isLoadCategories) {
                if (GlobalValue.myAccount != null) {
                    gotoActivity(MainTabActivity.class);
                    finish();
                } else {
                    gotoActivity(LoginActivity.class);
                    finish();
                }

            } else {
                handler.postDelayed(this, 1000);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferenceManager = MySharedPreferences.getInstance(SplashActivity.this);
        GlobalValue.myAccount = preferenceManager.getUserInfo();

        // remove notification count
        ShortcutBadger.applyCount(this, 0);

        // enable strict mode
        updateLanguage();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions()) {
                NetworkUtil.enableStrictMode();
                printKeyHash(this);
                gps = new GPSTracker(this);
                if (NetworkUtil.checkNetworkAvailable(this)) {
                    checkLocation();
                } else {
                    NetworkUtil.showSettingsAlert(this);
                }
            }
        } else {
            NetworkUtil.enableStrictMode();
            printKeyHash(this);
            gps = new GPSTracker(this);
            if (NetworkUtil.checkNetworkAvailable(this)) {
                checkLocation();
            } else {
                NetworkUtil.showSettingsAlert(this);
            }
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.e("Token", token);
                        preferenceManager.putStringValue(Constant.TOKEN_FCM, token);
                    }
                });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    private void checkLocation() {
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        } else {
            loadStaticData();
        }
    }


    //load city & category list
    private void loadStaticData() {
        ModelManager.getListCity(SplashActivity.this, true, new ModelManagerListener() {

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                if (ParserUtility.isSuccess(json))
                    new MySharedPreferences(self).setCacheCities(json);
                isLoadCity = true;
            }

            @Override
            public void onError(VolleyError error) {
                // TODO Auto-generated method stub
                isLoadCity = true;
            }
        });

        ModelManager.getListCategory(SplashActivity.this, true, new ModelManagerListener() {

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                if (ParserUtility.isSuccess(json))
                    new MySharedPreferences(self).setCacheCategories(json);
                isLoadCategories = true;
            }

            @Override
            public void onError(VolleyError error) {
                isLoadCategories = true;
            }
        });

        //checked loading data
        handler.post(r);

    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", "Key Hashes:" + context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private boolean checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(permission);
            }
        }

        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermission.toArray
                    (new String[listPermission.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                Boolean allPermissionsGranted = true;
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            allPermissionsGranted = false;
                            break;
                        }
                    }
                    if (!allPermissionsGranted) {
                        boolean somePermissionsForeverDenied = false;
                        boolean checkAllPermissonAllAlowed = true;
                        for (String permission : permissions) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                                //denied
                                checkAndRequestPermissions();
                                checkAllPermissonAllAlowed = false;
                                Log.e("denied", permission);
                            } else {
                                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                                    //allowed
                                    Log.e("allowed", permission);
                                } else {
                                    //set to never ask again
                                    Log.e("set to never ask again", permission);
                                    somePermissionsForeverDenied = true;
                                    checkAllPermissonAllAlowed = false;
                                    break;
                                }
                            }
                        }

                        if (somePermissionsForeverDenied) {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Permissions Required")
                                    .setMessage("You have forcefully denied some of the required permissions " +
                                            "for this action. Please open settings, go to permissions and allow them.")
                                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(1);
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        }
                        if (checkAllPermissonAllAlowed) {
                            NetworkUtil.enableStrictMode();
                            printKeyHash(this);
                            gps = new GPSTracker(this);
                            if (NetworkUtil.checkNetworkAvailable(this)) {
                                checkLocation();
                            } else {
                                NetworkUtil.showSettingsAlert(this);
                            }
                        }
                    } else {
                        NetworkUtil.enableStrictMode();
                        printKeyHash(this);
                        gps = new GPSTracker(this);
                        if (NetworkUtil.checkNetworkAvailable(this)) {
                            checkLocation();
                        } else {
                            NetworkUtil.showSettingsAlert(this);
                        }
                    }
                }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkAndRequestPermissions();
    }

    private void updateLanguage() {
        String language_code = "ar";
        Locale locale = new Locale(language_code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
