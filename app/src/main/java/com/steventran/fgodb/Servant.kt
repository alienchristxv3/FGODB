package com.steventran.fgodb

import com.google.gson.annotations.SerializedName

data class Servant(
    val collectionNo: Int,
    @SerializedName("name") val servantName: String,
    val className: String,
    val rarity: Int,
    val type: String,
    @SerializedName("face") val faceUrl: String) {



}