package com.onetick.pharmafest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BProduct {
    @SerializedName("ResponseCode")
    @Nullable
    private String responseCode;
    @SerializedName("ResponseMsg")
    @Nullable
    private String responseMsg;
    @SerializedName("Result")
    @Nullable
    private String result;
    @SerializedName("TotalPages")
    @Nullable
    private Integer TotalPages;
    @SerializedName("BrandProductList")
    @Nullable
    private List<Medicine> resultData;

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

    @Nullable
    public final List<Medicine> getResultData() {
        return this.resultData;
    }

    public final void setResultData(@Nullable List<Medicine> var1) {
        this.resultData = var1;
    }

    @Nullable
    public Integer getTotalPages() {
        return TotalPages;
    }

    public void setTotalPages(@Nullable Integer totalPages) {
        TotalPages = totalPages;
    }
}
