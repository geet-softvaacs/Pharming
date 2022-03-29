package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CategoryProduct {
    @SerializedName("id")
    @Expose
    @Nullable
    private String id;
    @SerializedName("category_name")
    @Expose
    @Nullable
    private String categoryName;
    @SerializedName("category_img")
    @Expose
    @Nullable
    private String categoryImg;
    @SerializedName("productlist")
    @Expose
    @Nullable
    private List<Medicine> productlist;

    @Nullable
    public final String getId() {
        return this.id;
    }

    public final void setId(@Nullable String var1) {
        this.id = var1;
    }

    @Nullable
    public final String getCategoryName() {
        return this.categoryName;
    }

    public final void setCategoryName(@Nullable String var1) {
        this.categoryName = var1;
    }

    @Nullable
    public final String getCategoryImg() {
        return this.categoryImg;
    }

    public final void setCategoryImg(@Nullable String var1) {
        this.categoryImg = var1;
    }

    @Nullable
    public final List<Medicine> getProductlist() {
        return this.productlist;
    }

    public final void setProductlist(@Nullable List<Medicine> var1) {
        this.productlist = var1;
    }
}
