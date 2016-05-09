package net.jspiner.assa.Model;

/**
 * Copyright 2015 JSpiner. All rights reserved.
 *
 * @author JSpiner (jspiner@naver.com)
 * @project MySmartRestaurant
 * @since 2015. 11. 14.
 */
public class CalData {
    int day;
    int year;
    int month;
    int dayofweek;
    String dayOfName;
    boolean selected;

    public CalData(int y, int m, int d, int h, String dayOfName) {
        year = y;
        month = m;
        day = d;
        dayofweek = h;
        this.dayOfName = dayOfName;
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getDayofweek() {
        return dayofweek;
    }


    public String getDayOfName() {
        return this.dayOfName;
    }
}

