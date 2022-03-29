package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteAddress {
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;
    @SerializedName("userid")
    @Expose
    private String userid;

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
