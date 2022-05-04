package com.libyasolutions.libyamarketplace.dialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.libyasolutions.libyamarketplace.BaseDialog;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.CategoryV3Adapter;
import com.libyasolutions.libyamarketplace.adapter.DepartmentAdapter;
import com.libyasolutions.libyamarketplace.object.CategorySearch;
import com.libyasolutions.libyamarketplace.object.CategoryV2;
import com.libyasolutions.libyamarketplace.object.CategoryV3;
import com.libyasolutions.libyamarketplace.object.DepartmentCategory;
import com.libyasolutions.libyamarketplace.object.MenuIdV2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DepartmentDialog extends BaseDialog {

    private static final String TAG = "DepartmentDialog";
    private static final String EXTRA_DEPARTMENT_CATEGORY_LIST = "LIST";

    @BindView(R.id.rv_department)
    RecyclerView rvDepartment;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.iv_arrow_bottom)
    ImageView ivArrowBottom;

    private List<DepartmentCategory> departmentCategoryList = new ArrayList<>();
    private List<CategoryV3> categoryV3List = new ArrayList<>();
    private List<CategorySearch> categorySearchList = new ArrayList<>();
    private List<MenuIdV2> menuIdV2List = new ArrayList<>();

    private CategoryV3Adapter categoryV3Adapter;
    private DepartmentAdapter departmentAdapter;
    private OnSearchByDepartmentListener listener;

    public static DepartmentDialog newInstance(List<DepartmentCategory> list) {
        DepartmentDialog fragment = new DepartmentDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_DEPARTMENT_CATEGORY_LIST,
                (ArrayList<? extends Parcelable>) list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            departmentCategoryList = getArguments()
                    .getParcelableArrayList(EXTRA_DEPARTMENT_CATEGORY_LIST);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.dialog_department;
    }

    @Override
    protected void configView() {
        // config recycler view
        rvDepartment.setHasFixedSize(true);
        rvDepartment.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        departmentAdapter = new DepartmentAdapter(getActivity(), departmentCategoryList, position -> {
                    String departmentId = departmentCategoryList.get(position).getDepartmentId();

                    if (departmentCategoryList.get(position).isChecked()) {
                        Log.e(TAG, " unchecked");
                        departmentCategoryList.get(position).setChecked(false);

                        removeListCategoryV3(departmentCategoryList.get(position));
                        for (Iterator<CategorySearch> iterator = categorySearchList.iterator(); iterator.hasNext(); ) {
                            CategorySearch categorySearch = iterator.next();
                            if (categorySearch.getDepartmentId().equals(departmentId)) {
                                iterator.remove();
                            } else {
                                Log.e(TAG, "don't remove");
                            }
                        }
                        Log.e(TAG, "category search list: " + categorySearchList.size());
                    } else {
                        Log.e(TAG, "checked");
                        departmentCategoryList.get(position).setChecked(true);

                        getListCategoryV3(position, departmentCategoryList.get(position));
                        categorySearchList.add(new CategorySearch(departmentId, ""));
                        Log.e(TAG, "category search list: " + categorySearchList.size());
                    }

                    if (categoryV3Adapter != null) {
                        // change category list
                        categoryV3Adapter.notifyDataSetChanged();

                        // hide category list if list = 0
                        if (categoryV3List != null && categoryV3List.size() < 1
                                && rvCategory.getVisibility() == View.VISIBLE) {
                            rvCategory.setVisibility(View.GONE);
                            ivArrowBottom.setVisibility(View.GONE);
                        }
                    }

                    departmentAdapter.notifyItemChanged(position);
                });
        rvDepartment.setAdapter(departmentAdapter);
    }

    @Override
    protected int getStyleDialog() {
        return 0;
    }

    @Override
    protected double initWidthDialog() {
        return 0.95;
    }

    @Override
    protected double initHeightDialog() {
        return 0;
    }

    @Override
    protected boolean isCancelOnTouchOutside() {
        return false;
    }

    @OnClick(R.id.btn_no)
    void chooseDismissDialog() {
        dismiss();
        categorySearchList.clear();
    }

    @OnClick(R.id.btn_search)
    void chooseSearchListHomeShop() {
        if (listener != null) {
            formatCategoryId(menuIdV2List);
            listener.onSearchByDepartment(categorySearchList);
        }
        dismiss();
    }

    @OnClick(R.id.container_department_category)
    void chooseShowCategoryFromDepartment() {
        if (categoryV3List.size() < 1) {
            showToast(R.string.noti_should_choose_department);
            if (rvCategory.getVisibility() == View.VISIBLE) {
                rvCategory.setVisibility(View.GONE);
            }
        } else {
            if (rvCategory.getVisibility() == View.GONE) {
                rvCategory.setVisibility(View.VISIBLE);
            }
            ivArrowBottom.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvCategory.setLayoutManager(layoutManager);

            categoryV3Adapter = new CategoryV3Adapter(getActivity(), categoryV3List,
                    (position, categoryV3) -> {

                        String menuId = categoryV3List.get(position).getMenuId();
                        String departmentId = categoryV3List.get(position).getDepartmentId();

                        if (categoryV3List.get(position).isChecked()) {
                            categoryV3List.get(position).setChecked(false);

                            // remove menuId CategorySearch
                            if (menuId != null && departmentId != null) {
                                removeMenuIdFromCategorySearch(menuId);
                            } else {
                                Log.e(TAG, "some error");
                            }

                        } else {
                            categoryV3List.get(position).setChecked(true);

                            // add menuId into CategorySearch
                            if (menuId != null && departmentId != null) {
                                addMenuIdFromCategorySearch(menuId, departmentId);
                            } else {
                                Log.e(TAG, "some error");
                            }

                        }

                        categoryV3Adapter.notifyItemChanged(position);
                    });
            rvCategory.setAdapter(categoryV3Adapter);

            rvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!recyclerView.canScrollVertically(1)) {
                        ivArrowBottom.setVisibility(View.GONE);
                    } else {
                        ivArrowBottom.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }


    public interface OnSearchByDepartmentListener {
        void onSearchByDepartment(List<CategorySearch> categorySearchList);
    }

    public void setOnSearchByDepartmentListener(OnSearchByDepartmentListener listener) {
        this.listener = listener;
    }

    private void addMenuIdFromCategorySearch(String menuId, String departmentId) {
        MenuIdV2 menuIdV2 = new MenuIdV2();
        menuIdV2.setMenuId(menuId);
        menuIdV2.setDepartmentId(departmentId);

        menuIdV2List.add(menuIdV2);
        Log.d(TAG, "menu Id list: " + menuIdV2List.size());
    }

    private void removeMenuIdFromCategorySearch(String menuId) {
        for (Iterator<MenuIdV2> iterator = menuIdV2List.iterator(); iterator.hasNext(); ) {
            MenuIdV2 menuIdV2 = iterator.next();
            if (menuIdV2.getMenuId().equals(menuId)) {
                iterator.remove();
            }
        }
        Log.e(TAG, "menu id list: " + menuIdV2List.size());
    }

    private void formatCategoryId(List<MenuIdV2> menuIdV2List) {
        for (CategorySearch categorySearch : categorySearchList) {
            for (MenuIdV2 menuIdV2 : menuIdV2List) {
                if (categorySearch.getDepartmentId().equals(menuIdV2.getDepartmentId())) {
                    String categoryId = categorySearch.getCategory();
                    categoryId = categoryId.concat(menuIdV2.getMenuId() + ",");
                    categorySearch.setCategory(categoryId);
                }
            }
        }
    }

    private void getListCategoryV3(int dPosition, DepartmentCategory departmentCategory) {
        String departmentPosition = String.valueOf(dPosition);
        String departmentId = departmentCategory.getDepartmentId();
        String departmentName = departmentCategory.getDepartmentName();

        List<CategoryV2> categoryV2List = departmentCategoryList.get(dPosition).getCategoryV2List();
        categoryV3List.add(new CategoryV3(departmentCategory.getDepartmentId(),
                departmentCategory.getDepartmentName(),
                true));
        if (categoryV2List != null && categoryV2List.size() > 0) {
            for (CategoryV2 categoryV2 : categoryV2List) {
                String menuId = categoryV2.getMenuId();
                String menuName = categoryV2.getMenuName();

                if (departmentId != null || departmentName != null
                        || menuId != null || menuName != null) {
                    CategoryV3 categoryV3 = new CategoryV3(
                            departmentPosition, departmentId,
                            departmentName, menuId, menuName);
                    categoryV3List.add(categoryV3);
                }
            }
        }
        Log.e(TAG, "v3 list: " + categoryV3List.size());
    }

    private void removeListCategoryV3(DepartmentCategory departmentCategory) {
        for (Iterator<CategoryV3> iterator = categoryV3List.iterator(); iterator.hasNext(); ) {
            CategoryV3 categoryV3 = iterator.next();
            String departmentId = departmentCategory.getDepartmentId();
            if (departmentId == null) {
                Log.e(TAG, "some error");
                return;
            }
            if (categoryV3.getDepartmentId().equals(departmentId)) {
                iterator.remove();
            } else {
                Log.e(TAG, "don't remove");
            }
        }
        Log.e(TAG, "v3 list: " + categoryV3List.size());
    }

}
