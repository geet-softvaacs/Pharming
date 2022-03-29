package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class Payment {
    @SerializedName("data")
    var data: List<PaymentItem>? = null

    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("Result")
    var result: String? = null
}