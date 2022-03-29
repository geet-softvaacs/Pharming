package com.onetick.pharmafest.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HomeData {
    @SerializedName("Banner")
    @Nullable
    private List<Banner> banner;
    @SerializedName("Brand")
    @Nullable
    private List<Brand> brand;
    @SerializedName("Catlist")
    @Nullable
    private List<CatList> catlist;
    @SerializedName("Main_Data")
    @Nullable
    private MainData mainData;
    @SerializedName("Medicine")
    @Nullable
    private List<Medicine> medicine;
    @SerializedName("testimonial")
    @Nullable
    private List<Testimonial> testimonial;
    @SerializedName("card_item_count")
    @Nullable
    private Integer cartItemCount;

    @Nullable
    public List<Banner>getBanner() {
        return banner;
    }

    public void setBanner(@Nullable List<Banner> banner) {
        this.banner = banner;
    }

    @Nullable
    public List<Brand> getBrand() {
        return brand;
    }

    public void setBrand(@Nullable List<Brand> brand) {
        this.brand = brand;
    }

    @Nullable
    public List<CatList> getCatlist() {
        return catlist;
    }

    public void setCatlist(@Nullable List<CatList> catlist) {
        this.catlist = catlist;
    }

    @Nullable
    public MainData getMainData() {
        return mainData;
    }

    public void setMainData(@Nullable MainData mainData) {
        this.mainData = mainData;
    }

    @Nullable
    public List<Medicine> getMedicine() {
        return medicine;
    }

    public void setMedicine(@Nullable List<Medicine> medicine) {
        this.medicine = medicine;
    }

    @Nullable
    public List<Testimonial> getTestimonial() {
        return testimonial;
    }

    public void setTestimonial(@Nullable List<Testimonial> testimonial) {
        this.testimonial = testimonial;
    }

    @Nullable
    public Integer getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(@Nullable Integer cartItemCount) {
        this.cartItemCount = cartItemCount;
    }
}
