package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.function.UnaryOperator;

public class CheckSlot {
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;
    @SerializedName("Status1")
    @Expose
    private List<Status1> status1 = null;
    @SerializedName("Status0")
    @Expose
    private List<UnAvailableSlot> status0 = null;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public List<Status1> getStatus1() {
        return status1;
    }

    public void setStatus1(List<Status1> status1) {
        this.status1 = status1;
    }

    public List<UnAvailableSlot> getStatus0() {
        return status0;
    }

    public void setStatus0(List<UnAvailableSlot> status0) {
        this.status0 = status0;
    }
}
