package com.onetick.pharmafest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public class CountryCode {
    @SerializedName("ccode")
    @Nullable
    private String ccode;
    @SerializedName("id")
    @Nullable
    private String id;
    @SerializedName("status")
    @Nullable
    private String status;

    @Nullable
    public final String getCcode() {
        return this.ccode;
    }

    public final void setCcode(@Nullable String var1) {
        this.ccode = var1;
    }

    @Nullable
    public final String getId() {
        return this.id;
    }

    public final void setId(@Nullable String var1) {
        this.id = var1;
    }

    @Nullable
    public final String getStatus() {
        return this.status;
    }

    public final void setStatus(@Nullable String var1) {
        this.status = var1;
    }
}
