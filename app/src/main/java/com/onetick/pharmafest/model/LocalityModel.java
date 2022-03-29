package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocalityModel {
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("PostOffice")
    @Expose
    private List<PostOfficeModel> postOffice = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PostOfficeModel> getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(List<PostOfficeModel> postOffice) {
        this.postOffice = postOffice;
    }
}
