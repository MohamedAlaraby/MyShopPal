package com.example.myshoppal.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class CartItem(
    val user_id:String="",
    val product_owner_id:String="",
    val product_id:String="",
    val title:String="",
    val price:String="",
    val image:String="",
   //cart_quantity-->>means how many items in your cart till now.
    var cart_quantity:String="",
    var stock_quantity:String="",
    var id:String=""
):Parcelable
