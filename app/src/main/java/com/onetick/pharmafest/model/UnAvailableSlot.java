package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnAvailableSlot {
    @SerializedName("status")
    @Expose
    private Integer slotStatus;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("avilable_slot")
    @Expose
    private List<AvailableSlot> avilableSlot = null;
    @SerializedName("title")
    @Expose
    private String title;

    public Integer getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(Integer slotStatus) {
        this.slotStatus = slotStatus;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public List<AvailableSlot> getAvilableSlot() {
        return avilableSlot;
    }

    public void setAvilableSlot(List<AvailableSlot> avilableSlot) {
        this.avilableSlot = avilableSlot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
