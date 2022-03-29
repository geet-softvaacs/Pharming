package com.onetick.pharmingo.model

import com.google.gson.annotations.SerializedName

class Couponlist {
    @SerializedName("c_desc")
    var cDesc: String? = null

    @SerializedName("c_img")
    var cImg: String? = null

    @SerializedName("c_t_mode")
    var cTMode: String? = null

    @SerializedName("coupon_code")
    var couponCode: String? = null

    @SerializedName("coupon_title")
    var couponTitle: String? = null

    @SerializedName("c_type")
    var cType: String? = null

    @SerializedName("c_value")
    var cValue: String? = null

    @SerializedName("cdate")
    var cdate: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("min_amt")
    var minAmt = 0

    @SerializedName("n_use")
    var nUse: String? = null
}