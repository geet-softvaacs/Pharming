package com.onetick.pharmafest.fragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestInfo {
    @SerializedName("product_price")
    @Expose
    private String testPrice;
    @SerializedName("product_discount")
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
