package com.onetick.pharmafest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LabCategorywithproduct {
    @SerializedName("ResponseCode")
    @Expose
    var responseCode: String? = null

    @SerializedName("Result")
    @Expose
    var result: String? = null

    @SerializedName("ResponseMsg")
    @Expose
    var responseMsg: String? = null

    @SerializedName("TotalPages")
    @Expose
    var TotalPages: Integer? = null

    @SerializedName("CategoryProduct")
    @Expose
    var categoryProduct: List<LabCategoryProduct>? = null
}