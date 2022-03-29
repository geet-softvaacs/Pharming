package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Home {
    @SerializedName("ResponseCode")
    @Expose
    private String response_code;
    @SerializedName("ResponseMsg")
    @Expose
    private String response_msg;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResultData")
    @Expose
    private HomeData homeData;

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getResponse_msg() {
        return response_msg;
    }

    public void setResponse_msg(String response_msg) {
        this.response_msg = response_msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HomeData getHomeData() {
        return homeData;
    }

    public void setHomeData(HomeData homeData) {
        this.homeData = homeData;
    }
}
