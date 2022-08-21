package com.example.myshoppal.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshoppal.R
import java.io.IOException

class GlideLoader(val context:Context) {
    fun loadUserPicture(image:Any,imageView: ImageView){
         try {
             //Load the user mage in the image view
             Glide
                 .with(context)
                 .load(Uri.parse(image.toString()))
                 .centerCrop()//scale type of the image
                 .placeholder(R.color.color_image_view_background)//a default place holder the loading fail
                 .into(imageView)

         }catch(ex:IOException){
            ex.printStackTrace()
         }
    }
    fun loadProductPicture(image:Any,imageView: ImageView){
        try {
            //Load the user mage in the image view
            Glide
                .with(context)
                .load(Uri.parse(image.toString()))
                .fitCenter()//scale type of the image
                .into(imageView)
        }catch(ex:IOException){
            ex.printStackTrace()
        }
    }
}