package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class Testimonial {
    @SerializedName("comment")
    var comment: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("img")
    var img: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("title")
    var title: String? = null
}