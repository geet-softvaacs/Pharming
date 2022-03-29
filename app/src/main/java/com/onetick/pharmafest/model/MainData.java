package com.onetick.pharmafest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public class MainData {
    @SerializedName("currency")
    @Nullable
    private String currency;
    @SerializedName("d_s_title")
    @Nullable
    private String dSTitle;
    @SerializedName("d_title")
    @Nullable
    private String dTitle;
    @SerializedName("id")
    @Nullable
    private String id;
    @SerializedName("logo")
    @Nullable
    private String logo;
    @SerializedName("one_hash")
    @Nullable
    private String oneHash;
    @SerializedName("one_key")
    @Nullable
    private String oneKey;
    @SerializedName("policy")
    @Nullable
    private String policy;
    @SerializedName("about")
    @Nullable
    private String about;
    @SerializedName("contact")
    @Nullable
    private String contact;
    @SerializedName("terms")
    @Nullable
    private String terms;
    @SerializedName("showpre")
    @Nullable
    private String showpre;

    @Nullable
    public final String getCurrency() {
        return this.currency;
    }

    public final void setCurrency(@Nullable String var1) {
        this.currency = var1;
    }

    @Nullable
    public final String getDSTitle() {
        return this.dSTitle;
    }

    public final void setDSTitle(@Nullable String var1) {
        this.dSTitle = var1;
    }

    @Nullable
    public final String getDTitle() {
        return this.dTitle;
    }

    public final void setDTitle(@Nullable String var1) {
        this.dTitle = var1;
    }

    @Nullable
    public final String getId() {
        return this.id;
    }

    public final void setId(@Nullable String var1) {
        this.id = var1;
    }

    @Nullable
    public final String getLogo() {
        return this.logo;
    }

    public final void setLogo(@Nullable String var1) {
        this.logo = var1;
    }

    @Nullable
    public final String getOneHash() {
        return this.oneHash;
    }

    public final void setOneHash(@Nullable String var1) {
        this.oneHash = var1;
    }

    @Nullable
    public final String getOneKey() {
        return this.oneKey;
    }

    public final void setOneKey(@Nullable String var1) {
        this.oneKey = var1;
    }

    @Nullable
    public final String getPolicy() {
        return this.policy;
    }

    public final void setPolicy(@Nullable String var1) {
        this.policy = var1;
    }

    @Nullable
    public final String getAbout() {
        return this.about;
    }

    public final void setAbout(@Nullable String var1) {
        this.about = var1;
    }

    @Nullable
    public final String getContact() {
        return this.contact;
    }

    public final void setContact(@Nullable String var1) {
        this.contact = var1;
    }

    @Nullable
    public final String getTerms() {
        return this.terms;
    }

    public final void setTerms(@Nullable String var1) {
        this.terms = var1;
    }

    @Nullable
    public final String getShowpre() {
        return this.showpre;
    }

    public final void setShowpre(@Nullable String var1) {
        this.showpre = var1;
    }
}
