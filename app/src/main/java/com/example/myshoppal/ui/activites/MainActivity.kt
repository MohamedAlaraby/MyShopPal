package com.example.myshoppal.ui.activites

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshoppal.R
import com.example.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences=getSharedPreferences(Constants.MY_SHOP_PAL_PREFRENCES,Context.MODE_PRIVATE)
        val userName=sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")
        tv_main_activity.text="Hello $userName."
    }
}