package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.CategoryAdapter;
import com.libyasolutions.libyamarketplace.adapter.CityAdapter;
import com.libyasolutions.libyamarketplace.adapter.SpinnerSimpleAdapter;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Category;
import com.libyasolutions.libyamarketplace.object.City;
import com.libyasolutions.libyamarketplace.object.SettingPreferences;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class PreferencesActivity extends BaseActivity implements OnClickListener {

    private Spinner spnCategories, spnCity, spnSortBy, spnSortType;
    private ArrayList<City> arrCities = new ArrayList<City>();
    private ArrayList<Category> arrCategory = new ArrayList<Category>();
    private CityAdapter cityAdapter;
    private CategoryAdapter categoryAdapter;
    private Button btnSavePreferences;
    private ImageView btnBack;
    private LinearLayout btnSelectShop, btnSelectMenu, btnSelectAll,
            btnSelectOpen;
    private ImageView imgticShop, imgticMenu, imgTickAll, imgTickOpen;
    private TextView lblMenu, lblShop, lblAll, lblOpen, lblDistance;
    private boolean isSelectShop = true;
    private boolean isOpen = false;

    private String cityId = "";
    private String categoryId = "";

    private SeekBar skbDistance;
    private SpinnerSimpleAdapter sortByAdapter, sortTypeAdapter;

    private static String ALL_OR_OPEN = SettingPreferences.ALL;
    private static String SORT_BY = SettingPreferences.SORT_BY_RATING;
    private static String SORT_TYPE = SettingPreferences.SORT_TYPE_DESC;

    private String distance = "0";
    private MySharedPreferences sharedPref;
    private SettingPreferences temPreferences, currentPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        sharedPref = new MySharedPreferences(this);
        setContentView(R.layout.activity_preferences);
        initUI();
        initControl();
        setData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            refreshContent();
        }
    }

    private void refreshContent() {
        if ((arrCategory.size() < 2) || (arrCities.size() < 2)) {
            setData();
        }
    }

    private void initUI() {
        spnCategories = (Spinner) findViewById(R.id.spnCategories);
        spnCity = (Spinner) findViewById(R.id.spnCity);
        spnSortBy = (Spinner) findViewById(R.id.spnSortBy);
        spnSortType = (Spinner) findViewById(R.id.spnSortType);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnSavePreferences = (Button) findViewById(R.id.btnSearch);
        btnSelectMenu = (LinearLayout) findViewById(R.id.btnSelectMenu);
        btnSelectShop = (LinearLayout) findViewById(R.id.btnSelectShop);
        btnSelectAll = (LinearLayout) findViewById(R.id.btnSelectAll);
        btnSelectOpen = (LinearLayout) findViewById(R.id.btnSelectOpen);
        imgticMenu = (ImageView) findViewById(R.id.imgticMenu);
        imgticShop = (ImageView) findViewById(R.id.imgticShop);
        imgTickAll = (ImageView) findViewById(R.id.imgTickAll);
        imgTickOpen = (ImageView) findViewById(R.id.imgTickOpen);
        lblMenu = (TextView) findViewById(R.id.lblMenu);
        lblShop = (TextView) findViewById(R.id.lblShop);
        lblAll = (TextView) findViewById(R.id.lblAll);
        lblOpen = (TextView) findViewById(R.id.lblOpen);
        lblDistance = (TextView) findViewById(R.id.lbl_distance);
        skbDistance = (SeekBar) findViewById(R.id.skb_distance);
        skbDistance.setMax(100);
    }

    private void initControl() {
        btnBack.setOnClickListener(this);
        btnSavePreferences.setOnClickListener(this);
        btnSelectMenu.setOnClickListener(this);
        btnSelectShop.setOnClickListener(this);
        btnSelectAll.setOnClickListener(this);
        btnSelectOpen.setOnClickListener(this);

        spnCity.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int index, long arg3) {
                if (index != 0) {
                    cityId = arrCities.get(index).getId() + "";
                } else {
                    cityId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnCategories.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int index, long arg3) {
                // TODO Auto-generated method stub
                if (index != 0) {
                    categoryId = arrCategory.get(index).getId() + "";
                } else {
                    categoryId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnSortBy.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0) {
                    SORT_BY = SettingPreferences.SORT_BY_RATING;
                } else if (position == 1) {
                    SORT_BY = SettingPreferences.SORT_BY_NAME;
                } else if (position == 2) {
                    SORT_BY = SettingPreferences.SORT_BY_DATE;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spnSortType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0) {
                    SORT_TYPE = SettingPreferences.SORT_TYPE_DESC;
                } else {
                    SORT_TYPE = SettingPreferences.SORT_TYPE_ASC;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        skbDistance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                distance = (double)progress/10 + "";
                if (progress==0) {
                    lblDistance.setText(getString(R.string.all));
                } else {
                    lblDistance.setText(distance + " Km");
                }
            }
        });
    }

    private void setData() {
        //get current preferences
        if (currentPreferences == null) {
            if (GlobalValue.myAccount.getPreferences() == null) {
                currentPreferences = new SettingPreferences();
                GlobalValue.myAccount.setPreferences(currentPreferences);
            } else {
                currentPreferences = GlobalValue.myAccount.getPreferences();
            }
        }
        SORT_BY = currentPreferences.getSortBy();
        SORT_TYPE = currentPreferences.getSortType();
        isSelectShop = currentPreferences.isShopType();
        isOpen = currentPreferences.isOpenShopList();
        distance = currentPreferences.getDistance();
        skbDistance.setProgress((int)(Double.parseDouble(distance)*10));

        // set up city
        arrCities.clear();
        arrCities.add(new City("0", "All Cities"));

        if (!sharedPref.getCacheCities().isEmpty()) {
            ArrayList<City> arr = ParserUtility.parseListCity(sharedPref.getCacheCities());
            arrCities.addAll(arr);
            setDataCityToList(arrCities);
        } else {
            ModelManager.getListCity(PreferencesActivity.this, true, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    String json = (String) object;
                    if (ParserUtility.isSuccess(json)) {
                        sharedPref.setCacheCities(json);
                        ArrayList<City> arr = ParserUtility.parseListCity(json);
                        arrCities.addAll(arr);
                    }
                    setDataCityToList(arrCities);
                }

                @Override
                public void onError(VolleyError error) {
                    // TODO Auto-generated method stub
                    setDataCityToList(arrCities);
                    Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                }
            });
        }

        // setup category
        arrCategory.clear();
        arrCategory.add(new Category("0", "All Categories"));
        if (!sharedPref.getCacheCategories().isEmpty()) {
            ArrayList<Category> arr = ParserUtility
                    .parseListCategories(sharedPref.getCacheCategories());
            arrCategory.addAll(arr);
            setDataCategoryToList(arrCategory);
        } else {
            ModelManager.getListCategory(this, true, new ModelManagerListener() {

                @Override
                public void onSuccess(Object object) {
                    String json = (String) object;
                    if (ParserUtility.isSuccess(json)) {
                        sharedPref.setCacheCategories(json);
                        ArrayList<Category> arr = ParserUtility
                                .parseListCategories(json);
                        arrCategory.addAll(arr);
                    }
                    setDataCategoryToList(arrCategory);

                }

                @Override
                public void onError(VolleyError error) {
                    // TODO Auto-generated method stub
                    setDataCategoryToList(arrCategory);
                    Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                }
            });
        }

        setSortByData();
        setSortTypeData();
        updateAllOpenButton();
        updateMenuShopButton();
    }

    private void updateMenuShopButton() {
        if (isSelectShop) {
            // set select shop
            btnSelectShop
                    .setBackgroundResource(R.drawable.bg_button_select_right);
            imgticShop.setVisibility(View.VISIBLE);
            lblShop.setTextColor(getResources().getColor(R.color.cl_white));
            // set unselect menu
            btnSelectMenu
                    .setBackgroundResource(R.drawable.bg_button_not_select_left);
            imgticMenu.setVisibility(View.GONE);
            lblMenu.setTextColor(getResources().getColor(R.color.black));
        } else {
            // set select shop
            btnSelectShop
                    .setBackgroundResource(R.drawable.bg_button_not_select_right);
            imgticShop.setVisibility(View.GONE);
            lblShop.setTextColor(getResources().getColor(R.color.black));
            // set unselect menu
            btnSelectMenu
                    .setBackgroundResource(R.drawable.bg_button_select_left);
            imgticMenu.setVisibility(View.VISIBLE);
            lblMenu.setTextColor(getResources().getColor(R.color.cl_white));
        }
    }

    private void updateAllOpenButton() {
        if (isOpen) {
            // set select shop
            ALL_OR_OPEN = SettingPreferences.OPEN;
            btnSelectOpen
                    .setBackgroundResource(R.drawable.bg_button_select_right);
            imgTickOpen.setVisibility(View.VISIBLE);
            lblOpen.setTextColor(getResources().getColor(R.color.cl_white));
            // set unselect menu
            btnSelectAll
                    .setBackgroundResource(R.drawable.bg_button_not_select_left);
            imgTickAll.setVisibility(View.GONE);
            lblAll.setTextColor(getResources().getColor(R.color.black));
        } else {
            // set select shop
            ALL_OR_OPEN = SettingPreferences.ALL;
            btnSelectOpen
                    .setBackgroundResource(R.drawable.bg_button_not_select_right);
            imgTickOpen.setVisibility(View.GONE);
            lblOpen.setTextColor(getResources().getColor(R.color.black));
            // set unselect menu
            btnSelectAll
                    .setBackgroundResource(R.drawable.bg_button_select_left);
            imgTickAll.setVisibility(View.VISIBLE);
            lblAll.setTextColor(getResources().getColor(R.color.cl_white));
        }
    }

    private void setDataCityToList(ArrayList<City> arrCity) {
        cityAdapter = new CityAdapter(self,
                android.R.layout.simple_spinner_item, arrCity);
        cityAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(cityAdapter);
        spnCity.setSelection(currentPreferences.selectedCityIndex(arrCity));
    }

    private void setDataCategoryToList(ArrayList<Category> arrCategory) {
        categoryAdapter = new CategoryAdapter(self,
                android.R.layout.simple_spinner_item, arrCategory);
        categoryAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategories.setAdapter(categoryAdapter);
        spnCategories.setSelection(currentPreferences.selectedCategoryIndex(arrCategory));
    }

    private void setSortByData() {
        sortByAdapter = new SpinnerSimpleAdapter(self,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.arr_sort_by));
        spnSortBy.setAdapter(sortByAdapter);
        spnSortBy.setSelection(currentPreferences.selectedSortByIndex());
    }

    private void setSortTypeData() {
        sortTypeAdapter = new SpinnerSimpleAdapter(self,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.arr_sort_type));
        spnSortType.setAdapter(sortTypeAdapter);
        spnSortType.setSelection(currentPreferences.selectedSortTypeIndex());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSelectMenu) {
            isSelectShop = false;
            updateMenuShopButton();
            return;
        }
        if (v == btnSelectShop) {
            isSelectShop = true;
            updateMenuShopButton();
            return;
        }
        if (v == btnSelectOpen) {
            ALL_OR_OPEN = SettingPreferences.OPEN;
            isOpen = true;
            updateAllOpenButton();
            return;
        }
        if (v == btnSelectAll) {
            ALL_OR_OPEN = SettingPreferences.ALL;
            isOpen = false;
            updateAllOpenButton();
            return;
        }
        if (v == btnSavePreferences) {
            onClickSavePreferences();
            return;
        }
        if (v == btnBack) {
            onBackPressed();
        }
    }

    private void onClickSavePreferences() {
        // TODO Auto-generated method stub
        temPreferences = new SettingPreferences();
        temPreferences.setUserId(GlobalValue.myAccount.getId());
        temPreferences.setCategoryId(categoryId);
        temPreferences.setCityID(cityId);
        temPreferences.setDistance(distance);
        temPreferences.setSortBy(SORT_BY);
        temPreferences.setSortType(SORT_TYPE);
        temPreferences.setType(isSelectShop ? SettingPreferences.TYPE_SHOP : SettingPreferences.TYPE_PRODUCTS);
        temPreferences.setStatus(ALL_OR_OPEN);

        ModelManager.updatePreferences(this, temPreferences, true, new ModelManagerListener() {
            @Override
            public void onError(VolleyError error) {
                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Object object) {
                if (ParserUtility.isSuccess(object.toString())) {
                    GlobalValue.myAccount.setPreferences(temPreferences);
                    sharedPref.setCacheUserInfo(ParserUtility.convertAccountToJsonString(GlobalValue.myAccount));
                    Toast.makeText(self, R.string.message_success, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(self, R.string.message_server_error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

}
