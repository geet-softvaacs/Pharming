package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class PrescriptionOrderH {
    @SerializedName("PrescriptionOrderProductList")
    var prescriptionOrderProductList: List<PrescriptionOrderProductList>? = null

    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("Result")
    var result: String? = null
}