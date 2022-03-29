package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class OrderHistory {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("order_date")
    var orderDate: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("total")
    var total: String? = null
}