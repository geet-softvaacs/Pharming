package com.onetick.pharmafest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public class Brand {
    @SerializedName("bname")
    @Nullable
    private String bname;
    @SerializedName("id")
    @Nullable
    private String id;
    @SerializedName("img")
    @Nullable
    private String img;
    @SerializedName("popular")
    @Nullable
    private String popular;
    @SerializedName("status")
    @Nullable
    private String status;

    @Nullable
    public final String getBname() {
        return this.bname;
    }

    public final void setBname(@Nullable String var1) {
        this.bname = var1;
    }

    @Nullable
    public final String getId() {
        return this.id;
    }

    public final void setId(@Nullable String var1) {
        this.id = var1;
    }

    @Nullable
    public final String getImg() {
        return this.img;
    }

    public final void setImg(@Nullable String var1) {
        this.img = var1;
    }

    @Nullable
    public final String getPopular() {
        return this.popular;
    }

    public final void setPopular(@Nullable String var1) {
        this.popular = var1;
    }

    @Nullable
    public final String getStatus() {
        return this.status;
    }

    public final void setStatus(@Nullable String var1) {
        this.status = var1;
    }
}
