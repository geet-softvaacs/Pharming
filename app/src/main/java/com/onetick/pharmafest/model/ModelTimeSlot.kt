package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class ModelTimeSlot(@SerializedName("from")
                    var from: String? = null,
                    var isClicked: Boolean = false,
                    var is_available: Boolean = true, var enabled: Boolean = true, var saloonSlotId:Int)