package com.onetick.pharmafest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPrice implements Parcelable {

    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_discount")
    @Expose
    private String productDiscount;

    protected ProductPrice(Parcel in) {
        productPrice = in.readString();
        productDiscount = in.readString();
    }



    public static final Creator<ProductPrice> CREATOR = new Creator<ProductPrice>() {
        @Override
        public ProductPrice createFromParcel(Parcel in) {
            return new ProductPrice(in);
        }

        @Override
        public ProductPrice[] newArray(int size) {
            return new ProductPrice[size];
        }
    };



    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(productPrice);
        dest.writeString(productDiscount);
    }

}
