package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestInfoItem {
    @SerializedName("test_price")
    @Expose
    private String testPrice;
    @SerializedName("test_discount")
    @Expose
    private String testDiscount;

    public String getTestPrice() {
        return testPrice;
    }

    public void setTestPrice(String testPrice) {
        this.testPrice = testPrice;
    }

    public String getTestDiscount() {
        return testDiscount;
    }

    public void setTestDiscount(String testDiscount) {
        this.testDiscount = testDiscount;
    }
}
