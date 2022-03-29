package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableSlot {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("slot_id")
    @Expose
    private String slotId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("ftest_id")
    @Expose
    private String ftestId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getFtestId() {
        return ftestId;
    }

    public void setFtestId(String ftestId) {
        this.ftestId = ftestId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
