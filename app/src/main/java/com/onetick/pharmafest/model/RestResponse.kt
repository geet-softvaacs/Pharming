package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class RestResponse {
    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("Result")
    var result: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("c_value")
    var c_value: Double? = null

    @SerializedName("Order_id")
    var Order_id: Integer? = null
}