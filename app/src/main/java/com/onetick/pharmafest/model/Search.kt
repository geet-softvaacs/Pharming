package com.onetick.pharmafest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.onetick.pharmafest.model.Medicine

class Search {
    @SerializedName("SearchData")
    @Expose
    var searchData: List<Medicine>? = null

    @SerializedName("ResponseCode")
    @Expose
    var responseCode: String? = null

    @SerializedName("Result")
    @Expose
    var result: String? = null

    @SerializedName("TotalPages")
    @Expose
    var TotalPages: Integer? = null

    @SerializedName("ResponseMsg")
    @Expose
    var responseMsg: String? = null
}