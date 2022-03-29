package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class Lab {

    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("Result")
    var result: String? = null

    @SerializedName("ResultData")
    var resultData: LabData? = null

}