package com.libyasolutions.libyamarketplace.object;

public class CategorySearch {
    private String departmentId;
    private String category;

    public CategorySearch() {
    }

    public CategorySearch(String departmentId, String categoryId) {
        this.departmentId = departmentId;
        this.category = categoryId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
