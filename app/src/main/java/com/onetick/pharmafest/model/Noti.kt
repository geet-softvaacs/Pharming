package com.onetick.pharmingo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Noti {
    @SerializedName("NotificationData")
    var notificationData: List<NotificationDatum>? = null

    @SerializedName("ResponseCode")
    var responseCode: String? = null

    @SerializedName("ResponseMsg")
    var responseMsg: String? = null

    @SerializedName("Result")
    var result: String? = null

    @SerializedName("TotalPages")
    @Expose
    var TotalPages: Integer? = null
}