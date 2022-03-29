package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onetick.pharmafest.fragment.TestInfo;

import java.util.List;

public class  Tests {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("test_name")
    @Expose
    private String testName;
    @SerializedName("test_image")
    @Expose
    private List<String> testImage = null;
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
    private TestInfo testInfo = null;

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

    public TestInfo getTestInfo() {
        return testInfo;
    }

    public void setTestInfo(TestInfo testInfo) {
        this.testInfo = testInfo;
    }
}
