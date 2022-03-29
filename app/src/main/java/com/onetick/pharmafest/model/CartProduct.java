package com.onetick.pharmafest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartProduct implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("product_amt")
    @Expose
    private String productAmt;

    @SerializedName("mrp")
    @Expose
    private String mrp;

    @SerializedName("slot_id")
    @Expose
    private String slot_id;

    @SerializedName("slot")
    @Expose
    private Slot slot;

    protected CartProduct(Parcel in) {
        id = in.readString();
        productId = in.readString();
        title = in.readString();
        image = in.readString();
        type = in.readString();
        qty = in.readString();
        cost = in.readString();
        discount = in.readString();
        productAmt = in.readString();
        mrp = in.readString();
        slot_id = in.readString();
    }

    public static final Creator<CartProduct> CREATOR = new Creator<CartProduct>() {
        @Override
        public CartProduct createFromParcel(Parcel in) {
            return new CartProduct(in);
        }

        @Override
        public CartProduct[] newArray(int size) {
            return new CartProduct[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String  getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProductAmt() {
        return productAmt;
    }

    public void setProductAmt(String productAmt) {
        this.productAmt = productAmt;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(productId);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(type);
        parcel.writeString(qty);
        parcel.writeString(cost);
        parcel.writeString(discount);
        parcel.writeString(productAmt);
        parcel.writeString(mrp);
        parcel.writeString(slot_id);
    }

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
