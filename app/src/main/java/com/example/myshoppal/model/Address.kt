package com.example.myshoppal.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    var id:String="",
    val user_id:String="",
    val name:String="",
    val phoneNumber:String="",
    val address:String="",
    val zipCode:String="",
    val additionalNote:String="",
    val otherDetails:String="",
    val type:String=""
):Parcelable
