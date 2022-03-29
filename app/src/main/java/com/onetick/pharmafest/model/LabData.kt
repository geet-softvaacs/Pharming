package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class LabData {
    @SerializedName("Banner")
    var banner: List<Banner>? = null

    @SerializedName("Catlist")
    var catlist: List<CatList>? = null

     @SerializedName("tests")
    var tests: List<Tests>? = null

    @SerializedName("Main_Data")
    var mainData: MainData? = null

    @SerializedName("testimonial")
    var testimonial: List<Testimonial>? = null

    @SerializedName("card_item_count")
    val cartCount: Int? = null
}