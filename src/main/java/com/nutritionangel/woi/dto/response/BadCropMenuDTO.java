package com.nutritionangel.woi.dto.response;

public class BadCropMenuDTO {
    private int year;
    private int month;
    private int day;

    private String food_name;
    private String bad_crop_name;
    private String alt_crop_name;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getBad_crop_name() {
        return bad_crop_name;
    }

    public void setBad_crop_name(String bad_crop_name) {
        this.bad_crop_name = bad_crop_name;
    }

    public String getAlt_crop_name() {
        return alt_crop_name;
    }

    public void setAlt_crop_name(String alt_crop_name) {
        this.alt_crop_name = alt_crop_name;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }
}
