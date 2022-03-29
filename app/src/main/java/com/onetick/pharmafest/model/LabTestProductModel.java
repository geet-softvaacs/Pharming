package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.bouncycastle.util.test.Test;

import java.util.List;

public class LabTestProductModel {
    @SerializedName("testlist")
    @Expose
    private List<Tests> testlist = null;

    public List<Tests> getTestlist() {
        return testlist;
    }

    public void setTestlist(List<Tests> testlist) {
        this.testlist = testlist;
    }
}
