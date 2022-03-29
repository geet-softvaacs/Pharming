package com.onetick.pharmafest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Medicine implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image")
    @Expose
    private ArrayList<String> productImage = null;
    @SerializedName("Brand_name")
    @Expose
    private String brandName;
    @SerializedName("preparation")
    @Expose
    private String preparation;

    @SerializedName("test")
    @Expose
    private String tests;

    @SerializedName("report")
    @Expose
    private String report;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;

    @SerializedName("product_info")
    @Expose
    private ArrayList<ProductPrice> productInfo = null;

    @SerializedName("timeSlot")
    @Expose
    private ArrayList<LabInfo> labInfo = null;

    @SerializedName("msalt")
    @Expose
    private String mSalt;

    protected Medicine(Parcel in) {
        id = in.readString();
        productName = in.readString();
        productImage = in.createStringArrayList();
        brandName = in.readString();
        shortDesc = in.readString();
        tests = in.readString();
        preparation = in.readString();
        report = in.readString();
        mSalt = in.readString();

    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ArrayList<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(ArrayList<String> productImage) {
        this.productImage = productImage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }


    public ArrayList<ProductPrice> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ArrayList<ProductPrice> productInfo) {
        this.productInfo = productInfo;
    }

    public ArrayList<LabInfo> getLabInfo() {
        return labInfo;
    }

    public void setLabInfo(ArrayList<LabInfo> labInfo) {
        this.labInfo = labInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(productName);
        dest.writeStringList(productImage);
        dest.writeString(brandName);
        dest.writeString(shortDesc);
        dest.writeString(tests);
        dest.writeString(preparation);
        dest.writeString(report);
        dest.writeString(mSalt);
    }


    public String getmSalt() {
        return mSalt;
    }

    public void setmSalt(String mSalt) {
        this.mSalt = mSalt;
    }
}

