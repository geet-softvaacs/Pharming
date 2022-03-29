package com.onetick.pharmafest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderH {
    @SerializedName("OrderHistory")
    var orderHistory: List<OrderHistory>? = null

    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("Result")
    var result: String? = null

    @SerializedName("TotalPages")
    @Expose
    var TotalPages: Integer? = null
}