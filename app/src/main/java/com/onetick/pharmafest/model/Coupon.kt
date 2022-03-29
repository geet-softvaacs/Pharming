package com.onetick.pharmingo.model

import com.google.gson.annotations.SerializedName

class Coupon {
    @SerializedName("couponlist")
    var couponlist: List<Couponlist>? = null

    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("Result")
    var result: String? = null
}