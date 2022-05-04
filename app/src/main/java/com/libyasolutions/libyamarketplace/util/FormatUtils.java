package com.libyasolutions.libyamarketplace.util;

import java.util.List;

public class FormatUtils {

    public static String formatCategoryId(List<String> menuIdList) {
        String categoryId = "";
        if (menuIdList.size() > 0) {
            for (int i = 0; i < menuIdList.size(); i++) {
                if (i <= menuIdList.size() - 2) {
                   categoryId = categoryId.concat(menuIdList.get(i) + ",");
                } else if (i == menuIdList.size() - 1) {
                   categoryId = categoryId.concat(menuIdList.get(i));
                }
            }
        }
        return categoryId;
    }

}
