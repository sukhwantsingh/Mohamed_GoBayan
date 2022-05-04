package com.libyasolutions.libyamarketplace.object;

import android.os.Build;

import java.util.Objects;

public class DepartmentPosition {
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public DepartmentPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentPosition)) return false;
        DepartmentPosition that = (DepartmentPosition) o;
        return getPosition() == that.getPosition();
    }
}
