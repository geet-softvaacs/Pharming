package com.onetick.pharmafest.model

data class LabTestCategory(
    val id: String = "",
    val categoryName: String = "",
    val categoryImg: String = "",
 val testlist: List<TestlistItem>?)