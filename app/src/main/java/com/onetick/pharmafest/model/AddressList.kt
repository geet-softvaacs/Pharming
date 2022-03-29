package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class AddressList {
    @SerializedName("address")
    var address: String? = null

    @SerializedName("delivery_charge")
    var deliveryCharge: String? = null

    @SerializedName("hno")
    var hno: String? = null

    @SerializedName("IS_UPDATE_NEED")
    var iSUPDATENEED: Boolean? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("landmark")
    var landmark: String? = null

    @SerializedName("lat_map")
    var latMap = 0.0

    @SerializedName("long_map")
    var longMap = 0.0

    @SerializedName("pincode")
    var pincode: String? = null

    @SerializedName("pincode_id")
    var pincodeId: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("uid")
    var uid: String? = null

    @SerializedName("address_image")
    var addressImage: String? = null

    @SerializedName("city")
    var city: String?=null

    @SerializedName("state")
    var state : String ?=null

    @SerializedName("name")
    var name: String?= null

    @SerializedName("postal_name")
    var postalname: String?=null

}