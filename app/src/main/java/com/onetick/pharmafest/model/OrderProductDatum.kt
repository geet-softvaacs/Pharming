package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class OrderProductDatum {
    @SerializedName("Product_discount")
    var productDiscount: String? = null

    @SerializedName("Product_image")
    var productImage: String? = null

    @SerializedName("Product_name")
    var productName: String? = null

    @SerializedName("Product_price")
    var productPrice: String? = null

    @SerializedName("Product_quantity")
    var productQuantity: String? = null

    @SerializedName("Product_total")
    var productTotal: String? = null

    @SerializedName("Product_variation")
    var productVariation: String? = null

    @SerializedName("slot")
    var slot:Slot?= null
}