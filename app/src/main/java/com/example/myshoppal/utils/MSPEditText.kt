package com.example.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView

class MSPEditText(context: Context, attrs: AttributeSet): AppCompatEditText(context, attrs) {
    init {
        applyFont()
    }
    private fun applyFont(){
        //this is used to get the file from the assets folder and set it to the title textview
        val typeFace= Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        typeface=typeFace
    }


}