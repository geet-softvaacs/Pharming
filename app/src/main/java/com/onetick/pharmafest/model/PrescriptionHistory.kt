package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class PrescriptionHistory {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("order_date")
    var orderDate: Any? = null

    @SerializedName("status")
    var status: String? = null
}