package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class OrderProductList {
    @SerializedName("Additional_Note")
    var additionalNote: String? = null

    @SerializedName("Coupon_Amount")
    var couponAmount: String? = null

    @SerializedName("customer_address")
    var customerAddress: String? = null

    @SerializedName("Delivery_charge")
    var deliveryCharge: String? = null

    @SerializedName("order_date")
    var orderDate: String? = null

    @SerializedName("Order_Product_Data")
    var orderProductData: List<OrderProductDatum>? = null

    @SerializedName("Order_Status")
    var orderStatus: String? = null

    @SerializedName("Order_SubTotal")
    var orderSubTotal: String? = null

    @SerializedName("Order_Total")
    var orderTotal: String? = null

    @SerializedName("Order_Transaction_id")
    var orderTransactionId: String? = null

    @SerializedName("p_method_name")
    var pMethodName: String? = null
}