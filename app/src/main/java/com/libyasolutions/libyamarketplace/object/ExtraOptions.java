package com.libyasolutions.libyamarketplace.object;

import java.util.ArrayList;

public class ExtraOptions {
    private ArrayList<OptionsItem> optionsItems;
    private String extraID;
    private String extraName;

    public ArrayList<OptionsItem> getOptionsItems() {
        return optionsItems;
    }

    public void setOptionsItems(ArrayList<OptionsItem> optionsItems) {
        this.optionsItems = optionsItems;
    }

    public String getExtraID() {
        return extraID;
    }

    public void setExtraID(String extraID) {
        this.extraID = extraID;
    }

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }
}
