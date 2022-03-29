package com.onetick.pharmingo.model

import com.google.gson.annotations.SerializedName

class NotificationDatum {
    @SerializedName("datetime")
    var datetime: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("uid")
    var uid: String? = null
}