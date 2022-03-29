package com.onetick.pharmafest.model;

public class CustomSlotModel {
    String slotTime, slotDate;
    String slotId;

    public CustomSlotModel(String slotTime, String slotId, String slotDate) {
        this.slotTime = slotTime;
        this.slotId = slotId;
        this.slotDate = slotDate;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(String slotDate) {
        this.slotDate = slotDate;
    }
}
