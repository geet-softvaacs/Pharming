package com.onetick.pharmafest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public class CatList {
    @SerializedName("id")
    @Nullable
    private String id;
    @SerializedName("catname")
    @Nullable
    private String catname;
    @SerializedName("catimg")
    @Nullable
    private String catimg;
    @SerializedName("count")
    private int count;

    @Nullable
    public final String getId() {
        return this.id;
    }

    public final void setId(@Nullable String var1) {
        this.id = var1;
    }

    @Nullable
    public final String getCatname() {
        return this.catname;
    }

    public final void setCatname(@Nullable String var1) {
        this.catname = var1;
    }

    @Nullable
    public final String getCatimg() {
        return this.catimg;
    }

    public final void setCatimg(@Nullable String var1) {
        this.catimg = var1;
    }

    public final int getCount() {
        return this.count;
    }

    public final void setCount(int var1) {
        this.count = var1;
    }

}
