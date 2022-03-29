package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class GetPincode {
    @SerializedName("Status1")
    var pincodeData: List<GetPincodeData>? = null
    @SerializedName("Status0")
    var unAvailable: List<GetPincodeData>? = null
}