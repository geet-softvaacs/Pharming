package com.onetick.pharmafest.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Categorywithproduct {
    @SerializedName("ResponseCode")
    @Expose
    @Nullable
    private String responseCode;
    @SerializedName("Result")
    @Expose
    @Nullable
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    @Nullable
    private String responseMsg;
    @SerializedName("CategoryProduct")
    @Expose
    @Nullable
    private List<CategoryProduct> categoryProduct;

    @SerializedName("TotalPages")
    @Expose
    @Nullable
    private Integer  TotalPages;

    @Nullable
    public final String getResponseCode() {
        return this.responseCode;
    }

    public final void setResponseCode(@Nullable String var1) {
        this.responseCode = var1;
    }

    @Nullable
    public final String getResult() {
        return this.result;
    }

    public final void setResult(@Nullable String var1) {
        this.result = var1;
    }

    @Nullable
    public final String getResponseMsg() {
        return this.responseMsg;
    }

    public final void setResponseMsg(@Nullable String var1) {
        this.responseMsg = var1;
    }

    @Nullable
    public final List<CategoryProduct> getCategoryProduct() {
        return this.categoryProduct;
    }

    public final void setCategoryProduct(@Nullable List<CategoryProduct> var1) {
        this.categoryProduct = var1;
    }

    @Nullable
    public Integer getTotalPages() {
        return TotalPages;
    }

    public void setTotalPages(@Nullable Integer totalPages) {
        TotalPages = totalPages;
    }
}
