package com.onetick.pharmafest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Codes {
    @SerializedName("CountryCode")
    @Nullable
    private List<CountryCode> countryCode = null;
    @SerializedName("ResponseCode")
    @Nullable
    private String responseCode;
    @SerializedName("ResponseMsg")
    @Nullable
    private String responseMsg;
    @SerializedName("Result")
    @Nullable
    private String result;

    @Nullable
    public final List<CountryCode> getCountryCode() {
        return this.countryCode;
    }

    public final void setCountryCode(@Nullable List var1) {
        this.countryCode = var1;
    }

    @Nullable
    public final String getResponseCode() {
        return this.responseCode;
    }

    public final void setResponseCode(@Nullable String var1) {
        this.responseCode = var1;
    }

    @Nullable
    public final String getResponseMsg() {
        return this.responseMsg;
    }

    public final void setResponseMsg(@Nullable String var1) {
        this.responseMsg = var1;
    }

    @Nullable
    public final String getResult() {
        return this.result;
    }

    public final void setResult(@Nullable String var1) {
        this.result = var1;
    }
}
