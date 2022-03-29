package com.onetick.pharmafest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PrescritionOrder {
    @SerializedName("PrescriptionHistory")
    var prescriptionHistory: List<PrescriptionHistory>? = null

    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("TotalPages")
    @Expose
    var TotalPages: Integer? = null

    @SerializedName("Result")
    var result: String? = null
}