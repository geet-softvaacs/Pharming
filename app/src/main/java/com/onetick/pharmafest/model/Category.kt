package com.onetick.pharmafest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Category {
    @SerializedName("CategoryData")
    @Expose
    var categoryData: List<CatList>? = null

    @SerializedName("ResponseCode")
    @Expose
    var responseCode: String? = null

    @SerializedName("TotalPages")
    @Expose
    var TotalPages: Integer? = null

    @SerializedName("Result")
    @Expose
    var result: String? = null

    @SerializedName("ResponseMsg")
    @Expose
    var responseMsg: String? = null
}