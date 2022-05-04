package com.libyasolutions.libyamarketplace.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.CommentAdapter;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Comment;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.widget.NoScrollListView;

import java.util.ArrayList;

public class ShopsCommentActivity extends BaseActivity implements
        OnClickListener {

    private Shop shop;
    private TextView lblShopName, lblReviewNumber;
    private Button btnReport, btnAddComment;
    private ImageView btnBack;
    private int shopId = -1;
    private RatingBar rtbRating;
    public static BaseActivity self;

    private NoScrollListView mLsvComment;
    private ArrayList<Comment> mArrComment;
    private CommentAdapter mCommentAdapter;

    private Button mBtnLoadmore;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_shops_comment);
        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initUI() {
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        rtbRating = (RatingBar) findViewById(R.id.rtbRating);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnReport = (Button) findViewById(R.id.btnReport);
        btnAddComment = (Button) findViewById(R.id.btnAddComment);
        lblReviewNumber = (TextView) findViewById(R.id.lblReviewNumber);
        mLsvComment = (NoScrollListView) findViewById(R.id.lsv_comment);
        // Add loadmore button
        mBtnLoadmore = (Button) getLayoutInflater().inflate(
                R.layout.loadmore_button, null);
        mLsvComment.addFooterView(mBtnLoadmore);

        // Should call this method at the end of declaring UI.
        initControl();
    }

    private void initControl() {
        btnBack.setOnClickListener(this);
        mBtnLoadmore.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnAddComment.setOnClickListener(this);
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(GlobalValue.KEY_SHOP_ID)) {
                shopId = b.getInt(GlobalValue.KEY_SHOP_ID);
            }
        }
        if (shopId != -1) {
            ModelManager.getShopById(self, shopId, true,
                    new ModelManagerListener() {

                        @Override
                        public void onSuccess(Object object) {

                            String json = (String) object;
                            shop = ParserUtility.parseShop(json);
                            if (shop != null) {
                                lblShopName.setText(shop.getShopName());
                                lblReviewNumber.setText(shop.getRateNumber() + " "+getString(R.string.reviews));
                                rtbRating.setRating((shop.getRateValue() / 2));
                            }

                        }

                        @Override
                        public void onError(VolleyError error) {
                            // TODO Auto-generated method stub
                            Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                        }
                    });

            // Get shop comments
            getComments(shopId + "");
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mBtnLoadmore) {
            loadMore();
        } else if (v == btnBack) {
            onBackPressed();
        } else if (v == btnReport) {
            onClickReport();
        } else if (v == btnAddComment) {
            onClickAddComment();
        }
    }

    private void onClickAddComment() {
        if (GlobalValue.myAccount == null) {
            CustomToast.showCustomAlert(self,
                    self.getString(R.string.message_no_login),
                    Toast.LENGTH_SHORT);
        } else {
            Bundle b = new Bundle();
            b.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
            gotoActivity(this, AddReviewActivity.class, b);
        }
    }

    private void onClickReport() {
        if (GlobalValue.myAccount == null) {
            CustomToast.showCustomAlert(self,
                    self.getString(R.string.message_no_login),
                    Toast.LENGTH_SHORT);
        } else {
            Bundle b = new Bundle();
            b.putString(GlobalValue.KEY_SHOP_ID, shop.getShopId() + "");
            b.putString(GlobalValue.KEY_SHOP_NAME,shop.getShopName());
            gotoActivity(this, FeedBackActivity.class, b);
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        self = null;
    }

    private void getComments(String id) {
        ModelManager.getShopsComments(self, id, page, false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        mArrComment = ParserUtility.parseComments(json);

                        // Show/hide 'Load more' button
                        if (mArrComment.size() % GlobalValue.COMMENT_PAGE == 0) {
//							mBtnLoadmore.setVisibility(View.VISIBLE);
                        } else {
                            mLsvComment.removeFooterView(mBtnLoadmore);
                        }

                        if (mArrComment != null && mArrComment.size() > 0) {
                            mCommentAdapter = new CommentAdapter(self,
                                    mArrComment, true);
                            mCommentAdapter.commentOnly = true;
                            mLsvComment.setAdapter(mCommentAdapter);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void loadMore() {
        page++;
        ModelManager.getShopsComments(self, shopId + "", page, false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        ArrayList<Comment> arrTemp = ParserUtility
                                .parseComments(json);

                        // Show/hide 'Load more' button
                        if (arrTemp != null
                                && arrTemp.size() % GlobalValue.COMMENT_PAGE == 0) {
//							mBtnLoadmore.setVisibility(View.VISIBLE);
                        } else {
                            mLsvComment.removeFooterView(mBtnLoadmore);
                        }

                        if (arrTemp != null && arrTemp.size() > 0) {
                            mArrComment.addAll(arrTemp);
                            mCommentAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(self,
                                    getString(R.string.you_had_all_of_comments_already),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }
}
