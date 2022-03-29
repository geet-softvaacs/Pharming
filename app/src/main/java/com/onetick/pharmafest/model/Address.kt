package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class Address {
    @SerializedName("AddressList")
    var addressList: List<AddressList>? = null

    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("Result")
    var result: String? = null
}