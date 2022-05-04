package com.libyasolutions.libyamarketplace.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ChatAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.object.Conversation;
import com.libyasolutions.libyamarketplace.object.MessengerFirebase;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshBase;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshListView;
import com.libyasolutions.libyamarketplace.util.FileDialog;
import com.libyasolutions.libyamarketplace.util.FirebaseUtils;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.RealPathUtil;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 5/29/2017.
 */


public class ChatDetailActivity extends BaseActivity implements AdapterListener.onLoad {
    private static final String TAG = ChatDetailActivity.class.getSimpleName();
    private PullToRefreshListView listView;
    private ImageView btnSend, btnSendFiles;
    private EditText edComment;
    private DatabaseReference ref;
    private String idAgent = "", title = "", image = "", shopId = "", buyerId = "", shopName = "", senderName = "";
    private String keyConversation = "";
    private ChatAdapter adapter;
    private ArrayList<MessengerFirebase> listMessages;
    private TextView lblTitle;
    private ImageView btnBack;
    private FileDialog fileDialog;
    private File sendFile;
    private MessengerFirebase message;
    private String urlFiles = "";
    private int i = 0;
    private int imageHeight = 0;
    private int imageWidth = 0;
    private int REQUEST_CODE_DOC = 8888;
    private Account userObj;
    private static final int CAPTURE_CAMERA = 101;
    private int countShop;
//    private InternetAvailabilityChecker internetAvailabilityChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        ref = FirebaseDatabase.getInstance().getReference();
        initUI();
        checkCreateConvention();
        initControl();
        InternetAvailabilityChecker.init(self);

    }

    public void checkCreateConvention() {

        Bundle b = getIntent().getBundleExtra("bundle");
        if (b == null) {
            idAgent = getIntent().getStringExtra("idAgent");
            title = getIntent().getStringExtra("title");
            image = getIntent().getStringExtra("image");
            shopId = getIntent().getStringExtra(Constant.SHOP_ID);
            shopName = getIntent().getStringExtra(Constant.SHOP_NAME);
            countShop = Integer.parseInt(getIntent().getStringExtra(Constant.COUNT_SHOP));
        } else {
            idAgent = b.getString("idAgent");
            title = b.getString("title");
            image = b.getString("image");
            shopId = b.getString(Constant.SHOP_ID);
            shopName = b.getString(Constant.SHOP_NAME);
            countShop = Integer.parseInt(b.getString(Constant.COUNT_SHOP));
        }
        lblTitle.setText(title);
        userObj = MySharedPreferences.getInstance(self).getUserInfo();

        if (userObj != null) {
            //set onFirebase is On Chat
            ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        keyConversation = child.getKey();
                        Log.e("keyConversation", "keyConversation:" + keyConversation);
                        ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).child(keyConversation).child("status").setValue("1");
                        ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).child(keyConversation).child("unread").setValue("0");

                    }
                    adapter = new ChatAdapter(ChatDetailActivity.this, keyConversation, image, idAgent);
                    listView.setAdapter(adapter);

                    Log.e(TAG, "list size: " + adapter.getCount());
                    buyerId = adapter.getBuyerId();
                    Log.e(TAG, "onDataChange: " + buyerId);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


        }

    }

    public void initUI() {
        listView = findViewById(R.id.lsvProperties);
        btnSend = findViewById(R.id.imgSend);
        btnSendFiles = findViewById(R.id.imgSendFiles);
        btnBack = findViewById(R.id.iv_back);
        edComment = findViewById(R.id.edComment);
        lblTitle = findViewById(R.id.lblTitle);

        edComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edComment.getText().toString().equals("")) {
                    btnSendFiles.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    btnSendFiles.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void initControl() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSendFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChooseImage();
//                browseDocuments();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edComment.getText().toString().equals("")) {
                    //Conversation
                    // User status 1 -> read
                    if (userObj != null) {
                        Map<String, Object> listConversation = new HashMap<String, Object>();
                        listConversation.put("senderId", userObj.getId());
                        listConversation.put("datePost", (System.currentTimeMillis() / 1000) + "");
                        listConversation.put("receiverId", idAgent);
                        listConversation.put("receiverName", title);
                        listConversation.put("lastMessage", edComment.getText().toString());
//                        listConversation.put("senderName", userObj.getFull_name());
                        listConversation.put("imageSender", userObj.getAvatar());
                        listConversation.put("status", "1");
                        listConversation.put("imageReceiver", image);
                        listConversation.put("unread", "0");
                        listConversation.put("shopId", shopId);
                        listConversation.put("shopName", shopName);
                        listConversation.put("countShop", countShop + "");
                        if (adapter.getBuyerId() != null && adapter.getBuyerId().length() != 0) {
                            buyerId = adapter.getBuyerId();
                            if (buyerId.equals(userObj.getId())) {
                                Log.e(TAG, "countShop: " + countShop);
                                if (countShop > 1) {
                                    senderName = "[" + shopName + "]:" + userObj.getFull_name();
                                    listConversation.put("senderName", "[" + shopName + "]:" + userObj.getFull_name());
                                } else {
                                    senderName = userObj.getFull_name();
                                    listConversation.put("senderName", userObj.getFull_name());
                                }

                            } else {
                                senderName = shopName;
                                listConversation.put("senderName", shopName);
                            }
                        } else {
                            buyerId = userObj.getId();
                            senderName = "[" + shopName + "]:" + userObj.getFull_name();
                            listConversation.put("senderName", "[" + shopName + "]:" + userObj.getFull_name());
                        }
                        listConversation.put("buyerId", buyerId);

                        if (keyConversation.equals("")) {
                            keyConversation = ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).push().getKey();
                            adapter = new ChatAdapter(ChatDetailActivity.this, keyConversation, image, idAgent);
                            listView.setAdapter(adapter);

                            Log.e(TAG, "list size: " + adapter.getCount());
                            buyerId = adapter.getBuyerId();
                            Log.e(TAG, "onDataChange: " + buyerId);
//                        adapter.notifyDataSetChanged();
                        }

                        ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).child(keyConversation).updateChildren(listConversation);

                        //Receiver User status 0 -> unread
                        final Map<String, Object> listConversationReceiver = new HashMap<String, Object>();
                        listConversationReceiver.put("senderId", userObj.getId());
                        listConversationReceiver.put("datePost", (System.currentTimeMillis() / 1000) + "");
                        listConversationReceiver.put("receiverId", idAgent);
                        listConversationReceiver.put("receiverName", title);
                        listConversationReceiver.put("lastMessage", edComment.getText().toString());
//                        listConversationReceiver.put("senderName", userObj.getFull_name());
                        listConversationReceiver.put("imageSender", userObj.getAvatar());
                        listConversationReceiver.put("imageReceiver", image);
                        listConversationReceiver.put("shopId", shopId);
                        listConversationReceiver.put("shopName", shopName);
                        listConversationReceiver.put("countShop", countShop + "");
                        if (adapter.getBuyerId() != null && adapter.getBuyerId().length() != 0) {
                            if (buyerId.equals(userObj.getId())) {
                                Log.e(TAG, "countShop: " + countShop);
                                if (countShop > 1) {
                                    senderName = "[" + shopName + "]:" + userObj.getFull_name();
                                    listConversationReceiver.put("senderName", "[" + shopName + "]:" + userObj.getFull_name());
                                } else {
                                    senderName = userObj.getFull_name();
                                    listConversationReceiver.put("senderName", userObj.getFull_name());
                                }
                            } else {
                                senderName = shopName;
                                listConversationReceiver.put("senderName", shopName);
                            }
                        } else {
                            senderName = "[" + shopName + "]:" + userObj.getFull_name();
                            buyerId = userObj.getId();
                            listConversationReceiver.put("senderName", "[" + shopName + "]:" + userObj.getFull_name());
                        }
                        listConversationReceiver.put("buyerId", buyerId);

                        //check Chat With If has count unRead => unread ++ else push unread = 1;
                        ref.child("user").child(idAgent).child("chatWith").child(userObj.getId() + shopId).child(keyConversation).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> listConversationReceivers = new HashMap<String, Object>();
                                final Map<String, Object> listData = (Map<String, Object>) dataSnapshot.getValue();

                                if (listData != null) {
                                    Conversation convert = dataSnapshot.getValue(Conversation.class);
                                    //to get unread
                                    int unread;
                                    if (convert.getUnread() != null) {
                                        unread = Integer.valueOf(convert.getUnread());
                                    } else {
                                        unread = 0;
                                    }
                                    if (listData.get("status") != null) {
                                        if (convert.getStatus().equals("1")) {
                                            listConversationReceivers.put("unread", "0");
                                        } else {
                                            listConversationReceivers.put("unread", String.valueOf(unread + 1));
                                        }
                                    } else {
                                        listConversationReceivers.put("unread", String.valueOf(unread + 1));
                                    }
                                    edComment.setText("");
                                } else {
                                    listConversationReceivers.put("unread", "1");
                                    edComment.setText("");
                                }
                                ref.child("user").child(idAgent).child("chatWith").child(userObj.getId() + shopId).child(keyConversation).updateChildren(listConversationReceivers);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                        ref.child("user").child(idAgent).child("chatWith").child(userObj.getId() + shopId).child(keyConversation).updateChildren(listConversationReceiver);

                        //Message
                        MessengerFirebase messengerFirebase = new MessengerFirebase();
                        messengerFirebase.setDatePost(System.currentTimeMillis() / 1000 + "");
                        messengerFirebase.setSenderId(userObj.getId());
                        messengerFirebase.setReceiverId(idAgent);
                        messengerFirebase.setMessage(edComment.getText().toString());
                        messengerFirebase.setUrlFile("");
                        messengerFirebase.setImageH(0 + "");
                        messengerFirebase.setImageW(0 + "");

                        String keyMessage = ref.child("message").child(keyConversation).push().getKey();
                        final Map<String, Object> messageFirebase = new HashMap<String, Object>();
                        messageFirebase.put("datePost", messengerFirebase.getDatePost());
                        messageFirebase.put("imageW", messengerFirebase.getImageW());
                        messageFirebase.put("imageH", messengerFirebase.getImageH());
                        messageFirebase.put("message", messengerFirebase.getMessage());
                        messageFirebase.put("receiverId", messengerFirebase.getReceiverId());
                        messageFirebase.put("senderId", messengerFirebase.getSenderId());
                        messageFirebase.put("urlFile", messengerFirebase.getUrlFile());
                        messageFirebase.put("shopId", shopId);
                        messageFirebase.put("shopName", shopName);
                        messageFirebase.put("countShop", countShop + "");
                        if (adapter.getBuyerId() != null && adapter.getBuyerId().length() != 0) {
                            buyerId = adapter.getBuyerId();
                            messageFirebase.put("buyerId", buyerId);
                        } else {
                            buyerId = userObj.getId();
                            messageFirebase.put("buyerId", buyerId);
                        }


                        ref.child("message").child(keyConversation).push().setValue(messageFirebase);


                        adapter.notifyDataSetChanged();
                        listView.getRefreshableView().smoothScrollToPosition(adapter.getCount() + 1);

//                        ModelManager.sendMessageIPhone(getBaseContext(),
//                                userObj.getId(), edComment.getText().toString(), idAgent, true, new ModelManagerListener() {
//                                    @Override
//                                    public void onError(VolleyError error) {
//                                    }
//
//                                    @Override
//                                    public void onSuccess(Object object) {
//                                    }
//                                });
                        ModelManager.sendMessageIphone(getBaseContext(), userObj.getId(), idAgent, edComment.getText().toString(), buyerId, String.valueOf(countShop), senderName, shopId, shopName,
                                false, new ModelManagerListener() {
                                    @Override
                                    public void onError(VolleyError error) {
                                    }

                                    @Override
                                    public void onSuccess(Object object) {
                                    }
                                });
                    } else {
                        showToastMessage(getResources().getString(R.string.please_enter_message));
                    }
                }
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter != null) {
                    if (!adapter.isLoadmore) {
                        final int currentPosition = adapter.currentSize;
                        Log.e("kevin", "onPullDownToRefresh: " + currentPosition);
                        adapter.getMoreData(listView);
                    } else {
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                listView.onRefreshComplete();
                            }
                        });

                        showToastMessage(getResources().getString(R.string.no_more_message));
                    }
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
    }

    // TODO: 09/08/2018 Toan:
    private void browseDocuments() {

        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip",
                        "audio/*",
                        "video/*",
                        "image/*"
                };

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            StringBuilder mimeTypesStr = new StringBuilder();
            for (String mimeType : mimeTypes) {
                mimeTypesStr.append(mimeType).append("|");
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), REQUEST_CODE_DOC);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DOC && resultCode == RESULT_OK) {
            Uri uri = data.getData(); //The uri with the location of the file
            File file = null;
            file = new File(RealPathUtil.getRealPath(ChatDetailActivity.this, uri));
            Log.e(TAG, "filePath: " + file.getPath());
            if (file.length() > (25 * 1024 * 1024)) {
                Toast.makeText(ChatDetailActivity.this, getString(R.string.files_size), Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Bitmap bitmap = ImageUtil.getCorrectlyOrientedImage(self, uri);
                    file = ImageUtil.saveBitmap(self,bitmap);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getPath(), options);

                    imageHeight = options.outHeight;
                    imageWidth = options.outWidth;
                    int rotate = getCameraPhotoOrientation(self, uri, file.getPath());
                    if (rotate == 90 || rotate == 270) {
                        imageHeight = options.outWidth;
                        imageWidth = options.outHeight;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                sendFile = file;
                if (NetworkUtil.checkNetworkAvailable(self)) {
//                    showDialog();
                    showProgressDialog();
                    new UploadFile().execute();
                } else {
                    showToastMessage(getResources().getString(R.string.no_connection));
                }

            }
        } else if (requestCode == CAPTURE_CAMERA && resultCode == Activity.RESULT_OK) {
            {
                try {

                    File file = new File(mCurrentPhotoPath);
                    Bitmap bitmap = MediaStore.Images.Media
                            .getBitmap(getContentResolver(), Uri.fromFile(file));
                    bitmap = getCorrectOrientation(mCurrentPhotoPath, bitmap);
                    if (bitmap != null) {
                        File fileTemp = ImageUtil.saveBitmap(self,bitmap);
                        bitmap = ImageUtil.getCorrectlyOrientedImage(self, Uri.fromFile(fileTemp));
                        sendFile = ImageUtil.saveBitmap(self,bitmap);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(sendFile.getPath(), options);
                        imageHeight = options.outHeight;
                        imageWidth = options.outWidth;
                    }
                    if (NetworkUtil.checkNetworkAvailable(self)) {
//                        showDialog();
                        showProgressDialog();
                        new UploadFile().execute();
                    } else {
                        showToastMessage(getResources().getString(R.string.no_connection));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        FirebaseUtils.changeStatus(this, "2");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!keyConversation.equals("")) {
            ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).child(keyConversation).child("status").setValue("0");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //setStatus for me
        if (!keyConversation.equals("")) {
            ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).child(keyConversation).child("unread").setValue("0");
            ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).child(keyConversation).child("status").setValue("1");
        }

        //cancel notification
        Intent i = new Intent(Constant.READ_MESSAGE);
        i.putExtra("idAgent", idAgent);
        sendBroadcast(i);
//        Utils.setBagde(getApplicationContext(), 0);

        //change Status
        FirebaseUtils.changeStatus(this, "1");
    }
//
//    @Override
//    public void onBackPressed() {
//
//        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
//
//    }

    @Override
    public void onLoad() {
        listView.getRefreshableView().setSelection(adapter.getCount());
    }

    private class UploadFile extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
//            return ModelManager.upLoadFile(getBaseContext(), userObj.getId(), sendFile);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "file: " + s);
//            closeDialog();
            closeProgressDialog();
//            if (JSONParser.isSuccess(s)) {
//                showToastMessage(getString(R.string.send_success));
//                String[] arr = JSONParser.getData(s).split("/");
//                urlFiles = arr[arr.length - 2] + "/" + arr[arr.length - 1];
//                setMessageFirebase(sendFile.getName(), urlFiles);
//            } else {
//                showToastMessage(JSONParser.getMessage(s));
//            }
        }
    }

    public void setMessageFirebase(final String messages, String url) {
        message = new MessengerFirebase();
        message.setSenderId(userObj.getId());
        message.setReceiverId(idAgent);
        message.setDatePost(System.currentTimeMillis() / 1000 + "");
        message.setMessage(messages);
        message.setUrlFile(url);
        message.setImageW(imageWidth + "");
        message.setImageH(imageHeight + "");

        Map<String, Object> listConversation = new HashMap<String, Object>();
        listConversation.put("senderId", userObj.getId());
        listConversation.put("datePost", (System.currentTimeMillis() / 1000) + "");
        listConversation.put("receiverId", idAgent);
        listConversation.put("receiverName", title);
        listConversation.put("lastMessage", messages);
        listConversation.put("senderName", userObj.getFull_name());
        listConversation.put("imageSender", userObj.getAvatar());
        listConversation.put("status", "1");
        listConversation.put("imageReceiver", image);
        listConversation.put("unread", "0");

        if (keyConversation.equals("")) {
            keyConversation = ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).push().getKey();
            adapter = new ChatAdapter(ChatDetailActivity.this, keyConversation, image, idAgent);
            listView.setAdapter(adapter);
        }

        ref.child("user").child(userObj.getId()).child("chatWith").child(idAgent + shopId).child(keyConversation).updateChildren(listConversation);

        //Receiver User status 0 -> unread
        final Map<String, Object> listConversationReceiver = new HashMap<String, Object>();
        listConversationReceiver.put("senderId", userObj.getId());
        listConversationReceiver.put("datePost", (System.currentTimeMillis() / 1000) + "");
        listConversationReceiver.put("receiverId", idAgent);
        listConversationReceiver.put("receiverName", title);
        listConversationReceiver.put("lastMessage", messages);
        listConversationReceiver.put("senderName", userObj.getFull_name());
        listConversationReceiver.put("imageSender", userObj.getAvatar());
        listConversationReceiver.put("imageReceiver", image);

        //check Chat With If has count unRead => unread ++ else push unread = 1;
        ref.child("user").child(idAgent).child("chatWith").child(userObj.getId() + shopId).child(keyConversation).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> listConversationReceivers = new HashMap<String, Object>();
                final Map<String, Object> listData = (Map<String, Object>) dataSnapshot.getValue();

                if (listData != null) {
                    Conversation convert = dataSnapshot.getValue(Conversation.class);
                    // check Unread if it's null
                    int unread;
                    if (convert.getUnread() != null) {
                        unread = Integer.valueOf(convert.getUnread());
                    } else {
                        unread = 0;
                    }
                    if (listData.get("status") != null) {
                        if (convert.getStatus().equals("1")) {
                            listConversationReceivers.put("unread", "0");
                        } else {
                            listConversationReceivers.put("unread", String.valueOf(unread + 1));
                        }
                    } else {
                        listConversationReceivers.put("unread", String.valueOf(unread + 1));
                    }

                    edComment.setText("");
                } else {
                    listConversationReceivers.put("unread", "1");
                    edComment.setText("");
                }
                ref.child("user").child(idAgent).child("chatWith").child(userObj.getId()).child(keyConversation).updateChildren(listConversationReceivers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        ref.child("user").child(idAgent).child("chatWith").child(userObj.getId()).child(keyConversation).updateChildren(listConversationReceiver);

        final Map<String, Object> messageFirebase = new HashMap<String, Object>();
        messageFirebase.put("datePost", message.getDatePost());
        messageFirebase.put("imageW", message.getImageW());
        messageFirebase.put("imageH", message.getImageH());
        messageFirebase.put("message", message.getMessage());
        messageFirebase.put("receiverId", message.getReceiverId());
        messageFirebase.put("senderId", message.getSenderId());
        messageFirebase.put("urlFile", message.getUrlFile());

        ref.child("message").child(keyConversation).push().setValue(messageFirebase);
        String sendtoIP = "";

        if (adapter.getFileType(message.getMessage())) {
            sendtoIP = "[Image]";
        } else {
            sendtoIP = "[File]";
        }

//        ModelManager.sendMessageIphone(getBaseContext(), userObj.getId(), idAgent, sendtoIP, buyerId, String.valueOf(countShop), senderName, shopId, shopName,
//                true, new ModelManagerListener() {
//                    @Override
//                    public void onError(VolleyError error) {
//                    }
//
//                    @Override
//                    public void onSuccess(Object object) {
//                    }
//                });
    }

    public String getKey() {
        return keyConversation;
    }

    private void showDialogChooseImage() {
        final CharSequence[] items = {
                getResources().getString(R.string.take_photo),
                getResources().getString(R.string.choose_from_library),
                getResources().getString(R.string.cancel_photo)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.take_photo))) {
                    takePhoto();
                } else if (items[item].equals(getResources().getString(R.string.choose_from_library))) {
                    browseDocuments();
                } else if (items[item].equals(getResources().getString(R.string.cancel_photo))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void takePhoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.hcpt.smartads.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_CAMERA);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Bitmap getCorrectOrientation(String photoPath, Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
}


