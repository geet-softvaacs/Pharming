package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class PrescriptionOrderProductList {
    @SerializedName("customer_address")
    var customerAddress: String? = null

    @SerializedName("order_date")
    var orderDate: String? = null

    @SerializedName("Order_Status")
    var orderStatus: String? = null

    @SerializedName("Prescription_image_list")
    var prescriptionImageList: List<String>? = null
}