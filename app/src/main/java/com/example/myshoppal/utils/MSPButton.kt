package com.example.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText

class MSPButton (context: Context, attrs: AttributeSet): AppCompatButton(context, attrs) {
    init {
        applyFont()
    }
    private fun applyFont(){
        //this is used to get the file from the assets folder and set it to the title textview
        val typeFace= Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        typeface=typeFace
    }
}