package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabTest {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("test_name")
    @Expose
    private String testName;
    @SerializedName("test_image")
    @Expose
    private List<String> testImage = null;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("test_info")
    @Expose
    private List<TestInfoItem> testInfo = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<String> getTestImage() {
        return testImage;
    }

    public void setTestImage(List<String> testImage) {
        this.testImage = testImage;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public List<TestInfoItem> getTestInfo() {
        return testInfo;
    }

    public void setTestInfo(List<TestInfoItem> testInfo) {
        this.testInfo = testInfo;
    }

}
