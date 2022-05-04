package com.libyasolutions.libyamarketplace.object;

import java.util.List;

public class MenuIdV2 {
    private String departmentId;
    private String menuId;

    public MenuIdV2() {
    }

    public MenuIdV2(String departmentId, String menuId) {
        this.departmentId = departmentId;
        this.menuId = menuId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
