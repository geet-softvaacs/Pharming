package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Status1 {
    @SerializedName("avilable_slot")
    @Expose
    private List<AvailableSlot> avilableSlot = null;

    public List<AvailableSlot> getAvilableSlot() {
        return avilableSlot;
    }

    public void setAvilableSlot(List<AvailableSlot> avilableSlot) {
        this.avilableSlot = avilableSlot;
    }
}
