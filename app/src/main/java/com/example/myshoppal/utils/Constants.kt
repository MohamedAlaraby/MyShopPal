package com.example.myshoppal.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    //our collections in the cloud firestore
    const val USERS="users"
    const val PRODUCTS="products"
    const val ADDRESSES="addresses"

    const val MY_SHOP_PAL_PREFRENCES:String="myshoppal_prefs"
    const val LOGGED_IN_USERNAME:String="loggedin_username"

    const val EXTRA_USER_DETAILS="extra_user_details"
    const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE=1
    const val PICK_IMAGE_REQUEST_CODE=2

    const val FIRST_NAME="first_name"
    const val LAST_NAME="last_name"
    const val MALE="male"
    const val FEMALE="female"
    const val MOBILE="mobile"
    const val GENDER="gender"
    const val IMAGE="image"

    const val PRODUCT_IMAGE="Product_Image"
    const val USER_ID="user_id"
    const val EXTRA_PRODUCT_ID="extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID="extra_product_owner_id"

    const val DEFAULT_CART_QUANTITY="1"

    const val CART_ITEMS:String="cart_items"
    const val CART_QUANTITY:String="cart_quantity"
    const val PRODUCT_ID:String="product_id"

    const val PROFILE_COMPLETED: String="profileCompleted"
    const val USER_PROFILE_IMAGE:String="user_profile_image"
    const val HOME="HOME"
    const val OFFICE="OFFICE"
    const val OTHER="OTHER"
    const val EXTRA_ADDRESS_DETAILS="AddressDetails"
    fun showImageChooser(activity: Activity){
        //an implicit intent to launch image selection of the phone storage
       val galleryIntent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
       activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
               .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}